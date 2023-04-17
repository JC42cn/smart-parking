package com.smart.smartparking.controller;

//import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Bookings;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.ParkingSpace;
import com.smart.smartparking.service.IParkingService;
import com.smart.smartparking.service.IParkingSpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
* <p>
*  前端控制器
* </p>
*
* @author  
* @since 2023-02-28
*/
@RestController
@Slf4j
@RequestMapping("/parking")
@Api(tags = "API停车场管理")
public class ParkingController {

    @Resource
    private IParkingService parkingService;

    @Resource
    private IParkingSpaceService parkingSpaceService;

    @ApiOperation(value = "新增停车场", notes = "新增停车场")
    @AutoLog("新增停车场")
    @PostMapping
    //@SaCheckPermission("parking.add")
    public Result save(@RequestBody Parking parking) {
        parkingService.save(parking);
        return Result.success("cg");
    }

    @ApiOperation(value = "编辑停车场", notes = "编辑停车场")
    @AutoLog("编辑停车场")
    @PutMapping
    //@SaCheckPermission("parking.edit")
    public Result update(@RequestBody Parking parking) {
        parkingService.updateById(parking);
        return Result.success("cg");
    }

    @ApiOperation(value = "删除停车场", notes = "删除停车场")
    @AutoLog("删除停车场")
    @DeleteMapping("/{id}")
    //@SaCheckPermission("parking.delete")
    public Result delete(@PathVariable Integer id) {
        parkingService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除停车场", notes = "批量删除停车场")
    @AutoLog("批量删除停车场")
    @PostMapping("/del/batch")
    //@SaCheckPermission("parking.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        parkingService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "停车场列表", notes = "停车场列表")
    @GetMapping("/all")
    //@SaCheckPermission("parking.list")
    public Result findAll() {
        return Result.success(parkingService.list());
    }

    @ApiOperation(value = "停车场列表2", notes = "停车场列表2")
    @GetMapping("/{id}")
    //@SaCheckPermission("parking.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(parkingService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    //@SaCheckPermission("parking.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<Parking>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(parkingService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @ApiOperation(value = "停车场列表", notes = "停车场列表")
    @GetMapping("/spNUmber")
    //@SaCheckPermission("parking.list")
    public List<Parking> findPakring(@RequestParam int pid) {
        QueryWrapper<Parking> qw = new QueryWrapper<>();
        List<Parking> pk = parkingService.list(qw.eq("pid",pid));
        return pk;
    }

    @ApiOperation(value = "停车场列表", notes = "停车场列表")
    @GetMapping("/allparking")
    //@SaCheckPermission("parking.list")
    public List<Parking> findParkingAll() {
        return parkingService.list();
    }

    @GetMapping("/findParkingNumber")
    public Result findParkingNumber(){
        return Result.success(parkingService.findParkingNumber());
    }

    @Scheduled(fixedRate = 5000)
    @GetMapping("/loadParkingCount")
    public void loadParkingCount() {
        List<Parking> parkings = parkingService.list();
        for (Parking parking : parkings) {
            int pid = parking.getPid(); // 获取当前 Parking 对象的 pid
            int psCount = parkingSpaceService.findParkingSpaceCountByPid(pid);
            Parking pk = parkingService.selectParkingByPid(pid);
            pk.setParkingSpaceNumber(psCount);
            //保存
            parkingService.updateById(pk);
        }


    }

}
