package com.smart.smartparking.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.smartparking.common.BaiDuUtils;
import com.smart.smartparking.common.CommonUtils;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.SystemConstant;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.Record;
import com.smart.smartparking.service.ICarService;
import com.smart.smartparking.service.IOrderService;
import com.smart.smartparking.service.IParkingService;
import com.smart.smartparking.service.IRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/distinguish")
@Api(tags = "图片文字识别")
public class DistinguishController {

    @Value("${file.path}")
    private String filePath;

    @Resource
    private BaiDuUtils baiDuUtils;

    @Autowired
    private ICarService carService;

    @Resource
    private IRecordService recordService;

    @Resource
    private IParkingService parkingService;
    @Resource
    private IOrderService orderService;

//    @Resource
//    private IOrderService orderService;
    @PostMapping("upload")
    @ApiOperation(value = "扫描车牌", notes = "扫描车牌")
    @AutoLog("文字识别")
    @SaCheckPermission("distinguish.upload")
    public Result upload(MultipartFile file,String name){
        try {
            QueryWrapper<Parking> parkingQueryWrapper = new QueryWrapper<>();
            Parking parking = new Parking();
            parking = parkingService.select(name);
            int id = parking.getId();
            if (file!=null) {
                parking = parkingService.getOne(parkingQueryWrapper.eq("pid",id));
                if (parking==null){
                    return Result.error("没找到该停车场");

                }
                File parentFile = CommonUtils.createParentFile(filePath);
                //获取文件名
                String fileName = file.getOriginalFilename();
                //获取文件后缀
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                log.info(suffix);
                //生成的是不带-的字符串
                String uuid = IdUtil.simpleUUID();
                fileName = uuid+suffix;

                File imageFile = new File(parentFile,fileName);

                FileUtil.writeFromStream(file.getInputStream(),imageFile);

                String fileDay = DateUtil.thisYear()+"/"+(DateUtil.thisMonth()+1)+"/"+DateUtil.thisDayOfMonth();

                String imagePath = SystemConstant.FILE + "/" + fileDay+"/"+fileName;

                String plateNumber = baiDuUtils.plateLicense(imageFile.getAbsolutePath());

                if(StringUtils.isBlank(plateNumber)){
                    return Result.error("识别失败");
                }
                Record record =new Record();

                if (recordService.getByPlateNumber(plateNumber,id)!=null){
                    //出库 查到信息
                    record=recordService.getByPlateNumber(plateNumber,id);
                    record.setOutTime(LocalDateTime.now());
                    Duration duration = Duration.between(record.getIntoTime(),record.getOutTime());
                    long second = Math.abs(duration.getSeconds());
                    Long hours = second/3600;
                    second=second-(hours*3600);
                    long minutes = second/60;
                    BigDecimal moeny = BigDecimal.ZERO;
                    //计算金额
                    if (hours>1){
                        if (minutes>0){
                            moeny=BigDecimal.valueOf(hours);
                        }else {
                            moeny=BigDecimal.valueOf(hours-1);
                        }
                    }else {//
                        if(hours==1 && minutes>0){
                            moeny=BigDecimal.ONE;
                        }else{
                            moeny=BigDecimal.ZERO;
                        }

                    }
                    record.setMoney(moeny);
                    Order order = new Order();
                    order.setMoney(moeny);

                    //生成订单号
                    long timestamp = System.currentTimeMillis();
                    Random random = new Random();
                    int randomNum = Math.abs(random.nextInt() % 1000000);
                    order.setOid(timestamp+randomNum);
                    order.setCarNumber(plateNumber);
                    Car car = carService.selectNumber(plateNumber);
                    order.setUid(car.getUid());
                    order.setPid(id);
                    record.setState(1);
                    //保存停车记录
                    recordService.updateById(record);
                    //生成订单
                    orderService.save(order);


                if(moeny == BigDecimal.ZERO){
                        return Result.success("出库成功");
                    }
                    return Result.success("识别成功，请支付");
                }else{

                    //入库
                    long timestamp = System.currentTimeMillis();
                    record.setRid(timestamp);
                    record.setPid(id);
                    record.setCarNumber(plateNumber);
                    recordService.save(record);
                    return Result.success("入库成功");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return Result.error();
        }
        return null;
    }
}
