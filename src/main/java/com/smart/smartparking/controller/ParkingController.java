package com.smart.smartparking.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.R;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.service.IParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/parking")
@Api(tags = "API停车场管理")
public class ParkingController {

    @Resource
    private IParkingService parkingService;

    @ApiOperation(value = "新增停车场", notes = "新增停车场", response = Order.class)
    @AutoLog("新增停车场")
    @PostMapping
    @SaCheckPermission("parking.add")
    public R save(@RequestBody Parking parking) {
        parkingService.save(parking);
        return R.success("cg");
    }

    @ApiOperation(value = "编辑停车场", notes = "编辑停车场", response = Order.class)
    @AutoLog("编辑停车场")
    @PutMapping
    @SaCheckPermission("parking.edit")
    public R update(@RequestBody Parking parking) {
        parkingService.updateById(parking);
        return R.success("cg");
    }

    @ApiOperation(value = "删除停车场", notes = "删除停车场", response = Order.class)
    @AutoLog("删除停车场")
    @DeleteMapping("/{id}")
    @SaCheckPermission("parking.delete")
    public R delete(@PathVariable Integer id) {
        parkingService.removeById(id);
        return R.success("cg");
    }

    @ApiOperation(value = "批量删除停车场", notes = "批量删除停车场", response = Order.class)
    @AutoLog("批量删除停车场")
    @PostMapping("/del/batch")
    @SaCheckPermission("parking.deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        parkingService.removeByIds(ids);
        return R.success("cg");
    }

    @ApiOperation(value = "停车场列表", notes = "停车场列表", response = Order.class)
    @GetMapping
    @SaCheckPermission("parking.list")
    public R findAll() {
        return R.success(parkingService.list());
    }

    @ApiOperation(value = "停车场列表2", notes = "停车场列表2", response = Order.class)
    @GetMapping("/{id}")
    @SaCheckPermission("parking.list")
    public R findOne(@PathVariable Integer id) {
        return R.success(parkingService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Order.class)
    @GetMapping("/page")
    @SaCheckPermission("parking.list")
    public R findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<Parking>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return R.success(parkingService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @ApiOperation(value = "导出", notes = "导出", response = Order.class)
    @GetMapping("/export")
    @SaCheckPermission("parking.export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Parking> list = parkingService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Parking信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
    * excel 导出
    * @param file
    * @throws Exception
    */
    @ApiOperation(value = "导出", notes = "导出", response = Order.class)
    @PostMapping("/import")
    @SaCheckPermission("parking.import")
    public R imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Parking> list = reader.readAll(Parking.class);

        parkingService.saveBatch(list);
        return R.success("cg");
    }

}
