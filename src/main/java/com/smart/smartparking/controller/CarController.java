package com.smart.smartparking.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.service.ICarService;
import com.smart.smartparking.service.UserService;
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
@RequestMapping("/car")
@Api(tags = "API车辆管理")
public class CarController {

    @Resource
    private ICarService carService;

    @Resource
    private UserService userService;


    @ApiOperation(value = "新增车辆", notes = "新增车辆")
    @AutoLog("新增车辆")
    @PostMapping
    public Result save(@RequestBody Car car) {
        Long uid = car.getUid();
        if (uid==0){
            return Result.error("请输入用户号");
        }else {
            car.setUid(uid);
            carService.save(car);
            log.info("新增车辆");
            return Result.success("新增车辆成功");
        }

    }

    @ApiOperation(value = "编辑车辆", notes = "编辑车辆", response = Car.class)
    @AutoLog("编辑车辆")
    @PutMapping
    //@SaCheckPermission("car.edit")
    public Result update(@RequestBody Car car) {
        carService.updateById(car);
        return Result.success("cg");
    }

    @ApiOperation(value = "删除车辆", notes = "删除车辆", response = Car.class)
    @AutoLog("删除车辆")
    @DeleteMapping("/{id}")
    //car.delete
    public Result delete(@PathVariable Integer id) {
        carService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除车辆", notes = "批量删除车辆", response = Car.class)
    @AutoLog("批量删除车辆")
    @PostMapping("/del/batch")
    //@SaCheckPermission("car.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "车辆列表", notes = "车辆列表", response = Car.class)
    @GetMapping
    //car.list
    public Result findAll() {
        return Result.success(carService.list());
    }

    @ApiOperation(value = "车辆列表2", notes = "车辆列表2", response = Car.class)
    @GetMapping("/{id}")
    //car.list
    public Result findOne(@PathVariable Integer id) {
        return Result.success(carService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Car.class)
    @GetMapping("/page")
    //car.list
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<Car>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "car_number", name);
        return Result.success(carService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @ApiOperation(value = "车辆列表uid", notes = "车辆列表uid")
    @GetMapping("/findCarByUid")
    //@SaCheckPermission("car.uidlist")
    public Result findCarByUid(@RequestParam(required = false) Integer uid,
                                 @RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<Car>().orderByDesc("id");
        queryWrapper.eq("uid", uid);
        //queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(carService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
