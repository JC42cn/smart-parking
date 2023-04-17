package com.smart.smartparking.controller;


//import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.smartparking.common.BaiDuUtils;
import com.smart.smartparking.common.CommonUtils;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.SystemConstant;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.*;
import com.smart.smartparking.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

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
    @Resource
    private BookingsService bookingsService;


    @Resource
    private IParkingSpaceService parkingSpaceService;

//    @Resource
//    private IOrderService orderService;
    @PostMapping("upload")
    @ApiOperation(value = "扫描车牌", notes = "扫描车牌")
    @AutoLog("文字识别")
    //@SaCheckPermission("distinguish.upload")
    public Result upload(MultipartFile file,String name,String username){//传入停车场名和图片
        try {

            //查询是否是管理员是否负责该停车场
            Parking pk = parkingService.findPakringByAdmin(username);
            String pName = pk.getName();

            if (!name.equals(pName)){
                return Result.error("801","不是你负责的停车场");
            }


            QueryWrapper<Parking> parkingQueryWrapper = new QueryWrapper<>();
            Parking parking = new Parking();
            //查找停车场名
            parking = parkingService.select(name);
            //获取停车场的id
            int id = parking.getId();
            //停车场收费金额
            BigDecimal parkingMoney = parking.getUnitCost();
            if (file!=null) {
                //图片不为空 查找pid
                parking = parkingService.getOne(parkingQueryWrapper.eq("pid",id));

                if (parking==null){
                    //pid为空返回前端
                    return Result.error("没找到该停车场");

                }
                //文件处理
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

                if (recordService.getByPlateNumber(plateNumber,id)!=null){ //出库 查到信息


                    record=recordService.getByPlateNumber(plateNumber,id);
                    record.setOutTime(LocalDateTime.now());

                    //获取停车场id
                    int parkingID = record.getPid();
                    //获取psnumber
                    int parkingSpaceID = record.getPsNumber();


                    //获取停车场免费时长
                    int freeTime =parking.getFreeTime();
                    //获取停车场收费单元
                    BigDecimal costUnit = parking.getUnitCost();

                    Duration duration = Duration.between(record.getIntoTime(),record.getOutTime());
                    long second = Math.abs(duration.getSeconds());
//                    int hours = (int) (second/3600);
//                    second=second-(hours*3600);
                    int minutes = (int)second/60;
                    BigDecimal moeny = BigDecimal.ZERO;
                    //把停车场收费金额转型
                    Double parkingMoney1 = parkingMoney.doubleValue();

                    if (minutes <= freeTime) {
                        moeny = BigDecimal.ZERO;
                    } else {
                        int billableMinutes = minutes - freeTime;
                        double cost = billableMinutes/60;
                        moeny = costUnit.multiply(BigDecimal.valueOf(cost));
                    }

                    Car car = carService.selectNumber(plateNumber);
                    record.setMoney(moeny);
                    Order order = new Order();
                    order.setMoney(moeny);
                    log.info(moeny+"=========================moeny==================");
                    //生成订单号
                    long timestamp = System.currentTimeMillis();
                    Random random = new Random();
                    int randomNum = Math.abs(random.nextInt() % 1000000);
                    order.setOid(timestamp+randomNum);
                    order.setCarNumber(plateNumber);
                    order.setUid(car.getUid());
                    order.setPid(id);
                    record.setState(1);
                    record.setOutTime(LocalDateTime.now());

                    //修改车位状态为0
                    ParkingSpace ps2 = parkingSpaceService.findParkingSpaceByPsNumber(parkingSpaceID,parkingID);
                    if(ps2 == null){
                        return Result.error("你还未记录车位！！！");
                    }
                    ps2.setState(0);

                    //保存车位状态
                    parkingSpaceService.updateById(ps2);
                    //保存停车记录
                    recordService.updateById(record);
                    //生成订单
                    orderService.save(order);



                if(moeny == BigDecimal.ZERO){
                    order.setState(1);
                    orderService.updateById(order);
                        return Result.success((plateNumber+"   成功出库"+name));
                    }
                    return Result.success("识别成功，请支付");

                //入库
                }else{
                    //查看数据库是否有该车
                    Car car = carService.selectNumber(plateNumber);
                    if( car==null){
                        return Result.error("800","未找到该车,请用户先注册！");
                    }
                    //查看是否有未支付的订单
                    Long uid = car.getUid();
                   Order orderPay = orderService.selectOrderPay(uid);
                   if (orderPay!=null){//为有未支付订单
                       return Result.error("你有一个未支付的订单请先支付");
                   }

                    long timestamp = System.currentTimeMillis();
                    record.setRid(timestamp);
                    record.setPid(id);
                    record.setCarNumber(plateNumber);
                    //入库，查询是否有预约
                    Bookings bk = bookingsService.findBookingsByUid(uid);

                    if(bk!=null && bk.getState()!=3){//有预约,入库后更改为已完成

                        //先查看预约是否是该停车场
                        int pID = bk.getPid();
                        int psNUMBER = bk.getPsNumber();
                        int bkState = bk.getState();
                        Parking pk1 = parkingService.selectParkingByPid(pID);
                        String pkName1 = pk1.getName();
                        log.info(pkName1+"==============pkName1=========================");
                        log.info(pName+"===================pName========================");

                        if (!pName.equals(pkName1)){
                            return Result.error("您预约的不是该停车场");
                        }

                        //修改预约状态
                        bk.setState(2);
                        bookingsService.updateById(bk);
                        log.info("修改预约状态为2，已完成");

                        //修改车位状态
                        ParkingSpace ps1 = parkingSpaceService.findParkingSpaceByPsNumber(psNUMBER,pID);
                        ps1.setState(1);//修改车位状态
                        parkingSpaceService.updateById(ps1);
                        log.info("修改车位状态" +
                                " 1");
                        record.setPsNumber(psNUMBER);//记录车位号
                        parkingService.updateById(parking);
                        //生成记录
                        recordService.save(record);
                        return  Result.success(plateNumber+" 完成预约");

                    }else {
                        //没有预约或者预约过期
                        parkingService.updateById(parking);
                        //生成记录
                        recordService.save(record);
                        return Result.success(plateNumber+"   成功入库"+name);
                    }

                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return Result.error();
        }
        return null;
    }

    @PostMapping("uploadcar")
    @ApiOperation(value = "扫描车牌", notes = "扫描车牌")
    @AutoLog("文字识别")
    //@SaCheckPermission("distinguish.uploadcar")
    public Result uploadCarNumber(MultipartFile file){
        try {
            //文件处理
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
            return Result.success(plateNumber);

        }catch (Exception ex){
            ex.printStackTrace();
            return Result.error(ex.getMessage());
        }
    }

}
