package com.smart.smartparking.controller;

//import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.ParkingSpace;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.IParkingService;
import com.smart.smartparking.service.IParkingSpaceService;
import com.smart.smartparking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
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
@RestController
@RequestMapping("/parkingSpace")
@Api(tags = "API停车位管理")
public class ParkingSpaceController {

    @Resource
    private UserService userService;
    @Resource
    private IParkingSpaceService parkingSpaceService;
    @Resource
    private IParkingService parkingService;

    @ApiOperation(value = "新增停车位", notes = "新增停车位", response = Order.class)
    @AutoLog("新增停车位")
    @PostMapping
    //@SaCheckPermission("parkingSpace.add")
    public Result save(@RequestBody ParkingSpace parkingSpace) {
        int pid = parkingSpace.getPid();
       ParkingSpace ps = parkingSpaceService.findPaekingSpace(parkingSpace);
       if (ps!=null){
           return Result.error("800","该车位已存在！");
       }
       Parking parking =  parkingService.selectParkingByPid(pid);
       if (parking==null){
           return Result.error("801","停车场不存在!");
       }
        int psnumber = parkingService.selectParkingSpaceCount(parking.getName());
       int state = ps.getState();
       if (state == 0){
           psnumber = psnumber+1;
           parking.setParkingSpaceNumber(psnumber);
           parkingService.updateById(parking);
       }
        parkingSpaceService.save(parkingSpace);
        return Result.success("新增车位成功");
    }

    @ApiOperation(value = "编辑停车位", notes = "编辑停车位", response = Order.class)
    @AutoLog("编辑停车位")
    @PutMapping
    //@SaCheckPermission("parkingSpace.edit")
    public Result update(@RequestBody ParkingSpace parkingSpace) {
        int state  = parkingSpace.getState();
        Parking pk = parkingService.selectParkingByPid( parkingSpace.getPid());
        int psnumber = pk.getParkingSpaceNumber();
        parkingSpaceService.updateById(parkingSpace);
        return Result.success("修改成功");
    }

    @ApiOperation(value = "删除停车位", notes = "删除停车位", response = Order.class)
    @AutoLog("删除停车位")
    @DeleteMapping("/{id}")
    //@SaCheckPermission("parkingSpace.delete")
    public Result delete(@PathVariable Integer id) {
        ParkingSpace ps = parkingSpaceService.findParkingSpaceById(id);
        parkingSpaceService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "批量删除停车位", notes = "批量删除停车位", response = Order.class)
    @AutoLog("批量删除停车位")
    @PostMapping("/del/batch")
   // @SaCheckPermission("parkingSpace.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        parkingSpaceService.removeByIds(ids);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "停车位列表", notes = "停车位列表", response = Order.class)
    @GetMapping
    //@SaCheckPermission("parkingSpace.list")
    public Result findAll() {
        return Result.success(parkingSpaceService.list());
    }

    @ApiOperation(value = "停车位列表2", notes = "停车位列表2", response = Order.class)
    @GetMapping("/{id}")
    //@SaCheckPermission("parkingSpace.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(parkingSpaceService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Order.class)
    @GetMapping("/page")
    //@SaCheckPermission("parkingSpace.list")
    public Result findPage(@RequestParam(defaultValue = "") String psNumber,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<ParkingSpace>().orderByDesc("id");
        queryWrapper.like(!"".equals(psNumber), "ps_number", psNumber);
        return Result.success(parkingSpaceService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/adminpage")
    //@SaCheckPermission("parkingSpace.list")
    public Result adminpage(@RequestParam(defaultValue = "") int  name,
                           @RequestParam Integer pageNum,
                            @RequestParam(required = false) Long uid,
                           @RequestParam Integer pageSize) {
        User user = userService.findUserByUid(uid);
        String username = user.getName();
        Parking parking = parkingService.findPakringByAdmin(username);
        int pid = parking.getPid();
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<ParkingSpace>().orderByDesc("id");
        log.info(name+"name=================================================>");
        if (name==0){
            queryWrapper.eq("pid",pid);
        }else {
            queryWrapper.eq("ps_number", name).eq("pid",pid);
        }
        return Result.success(parkingSpaceService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
