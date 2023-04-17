package com.smart.smartparking.controller;

//import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.*;
import com.smart.smartparking.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;


/**
* <p>
*  前端控制器
* </p>
*
* @author  
* @since 2023-02-28
*/
@Slf4j
@Api(tags = "API停车记录")
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private IRecordService recordService;
    @Resource
    private IParkingService parkingService;
    @Resource
    private IParkingSpaceService parkingSpaceService;

    @Resource
    private ICarService carService;
    @Resource
    private UserService userService;

    @ApiOperation(value = "新增停车记录", notes = "新增停车记录", response = Car.class)
    @AutoLog("新增停车记录")
    @PostMapping
    //@SaCheckPermission("record.add")
    public Result save(@RequestBody Record record) {
        long timestamp = System.currentTimeMillis();
        record.setRid(timestamp);
        recordService.save(record);
        return Result.success("cg");
    }

    @ApiOperation(value = "编辑停车记录", notes = "编辑停车记录")
    @AutoLog("编辑停车记录")
    @PutMapping
    //@SaCheckPermission("record.edit")
    public Result update(@RequestBody Record record) {
        try{
            if (record.getRid()==0){
                return Result.error("保存失败，记录号为空！");
            }
            int psNumber = record.getPsNumber();
            int pid  = record.getPid();
            ParkingSpace ps = parkingSpaceService.findParkingSpaceByPsNumber(psNumber,pid);
            if (ps==null){
                return  Result.error("没找到该车位");
            }else{//改变停车位改变车位状态
                ps.setState(1);
                parkingSpaceService.updateById(ps);
                recordService.updateById(record);
                return Result.success();
            }
        }catch (Exception ex){
            return Result.error("保存失败！");
        }


    }



    @ApiOperation(value = "删除停车记录", notes = "删除停车记录", response = Car.class)
    @AutoLog("删除停车记录")
    @DeleteMapping("/{id}")
    //@SaCheckPermission("record.delete")
    public Result delete(@PathVariable Integer id) {
        recordService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除停车记录", notes = "批量删除停车记录", response = Car.class)
    @AutoLog("批量删除停车记录")
    @PostMapping("/del/batch")
    //@SaCheckPermission("record.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        recordService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "车辆记录列表", notes = "车辆记录列表", response = Car.class)
    @GetMapping
    //@SaCheckPermission("record.list")
    public Result findAll() {
        return Result.success(recordService.list());
    }

    @ApiOperation(value = "车辆记录列表2", notes = "车辆记录列表2", response = Car.class)
    @GetMapping("/{id}")
    //@SaCheckPermission("record.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(recordService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Car.class)
    @GetMapping("/page")
    //@SaCheckPermission("record.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<Record>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "car_number", name);
        return Result.success(recordService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @ApiOperation(value = "导出", notes = "导出", response = Car.class)
    @GetMapping("/export")
    //@SaCheckPermission("record.export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Record> list = recordService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Record信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
    * excel 导入
    * @param file
    * @throws Exception
    */
    @ApiOperation(value = "导入", notes = "导入", response = Car.class)
    @PostMapping("/import")
    //@SaCheckPermission("record.import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Record> list = reader.readAll(Record.class);

        recordService.saveBatch(list);
        return Result.success("cg");
    }


    @ApiOperation(value = "记录列表uid2", notes = "记录列表uid2")
    @GetMapping("/findRecordByUid")
    //@SaCheckPermission("record.uidlist")
    public Result findRecordByUid(@RequestParam(required = false) Integer uid,
                                 @RequestParam Integer pageNum,
                                  @RequestParam String intoTime,
                                  @RequestParam String outTime,
                                 @RequestParam Integer pageSize) {
        String carNumber  = carService.selectCarNumber(uid);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<Record>().orderByDesc("id");
        queryWrapper.eq("car_number",carNumber);
        queryWrapper.like(!"".equals(intoTime), "into_time", intoTime)
                .like(!"".equals(outTime), "out_time", outTime);
        return Result.success(recordService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @ApiOperation(value = "订单列表根据admin", notes = "订单列表根据admin")
    @GetMapping("/findRecordByAdmin")
    public Result findRecordByAdmin(@RequestParam(defaultValue = "") String name,
                                 @RequestParam String intoTime,
                                 @RequestParam String outTime,
                                 @RequestParam Long uid,
                                 @RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize) {
        User user = userService.findUserByUid(uid);
        String username = user.getName();
        Parking parking = parkingService.findPakringByAdmin(username);
        int pid = parking.getPid();
        QueryWrapper<Record> queryWrapper = new QueryWrapper<Record>().orderByDesc("id");
        queryWrapper.eq("pid", pid);
        queryWrapper.like(!"".equals(name), "car_number", name).
                like(!"".equals(intoTime), "into_time", intoTime).
                like(!"".equals(outTime), "out_time", outTime);
        return Result.success(recordService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @ApiOperation(value = "", notes = "")
    @GetMapping("/getRecordCountByDay")
    public Result getRecordAllByDay( @RequestParam int day){
        String today = String.valueOf(LocalDate.now());
        return Result.success(recordService.findRecordCountByDay(today,day));
    }

    @ApiOperation(value = "", notes = "")
    @GetMapping("/findRecordCount")
    public Result findOrderCount(){
        return Result.success(recordService.findRecordCount());
    }

//    @ApiOperation(value = "", notes = "")
//    @GetMapping("/getRecordCountByDay")
//    public Result getRecordAllByDay( @RequestParam int day){
//        String today = String.valueOf(LocalDate.now());
//        int recordCounts = recordService.findRecordCountByDay(today,day);;
//        return Result.success(recordCounts);
//    }

}
