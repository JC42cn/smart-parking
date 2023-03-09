package com.smart.smartparking.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.ParkingSpace;
import com.smart.smartparking.service.IParkingSpaceService;
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
@RequestMapping("/parkingSpace")
@Api(tags = "API停车位管理")
public class ParkingSpaceController {

    @Resource
    private IParkingSpaceService parkingSpaceService;

    @ApiOperation(value = "新增停车位", notes = "新增停车位", response = Order.class)
    @AutoLog("新增停车位")
    @PostMapping
    @SaCheckPermission("parkingSpace.add")
    public Result save(@RequestBody ParkingSpace parkingSpace) {
//        User user = SessionUtils.getUser();
//        parkingSpace.setUser(user.getName());
//        parkingSpace.setUserid(user.getId());
//        parkingSpace.setDate(DateUtil.today());
//        parkingSpace.setTime(DateUtil.now());
        parkingSpaceService.save(parkingSpace);
        return Result.success("cg");
    }

    @ApiOperation(value = "编辑停车位", notes = "编辑停车位", response = Order.class)
    @AutoLog("编辑停车位")
    @PutMapping
    @SaCheckPermission("parkingSpace.edit")
    public Result update(@RequestBody ParkingSpace parkingSpace) {
        parkingSpaceService.updateById(parkingSpace);
        return Result.success("cg");
    }

    @ApiOperation(value = "删除停车位", notes = "删除停车位", response = Order.class)
    @AutoLog("删除停车位")
    @DeleteMapping("/{id}")
    @SaCheckPermission("parkingSpace.delete")
    public Result delete(@PathVariable Integer id) {
        parkingSpaceService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除停车位", notes = "批量删除停车位", response = Order.class)
    @AutoLog("批量删除停车位")
    @PostMapping("/del/batch")
    @SaCheckPermission("parkingSpace.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        parkingSpaceService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "停车位列表", notes = "停车位列表", response = Order.class)
    @GetMapping
    @SaCheckPermission("parkingSpace.list")
    public Result findAll() {
        return Result.success(parkingSpaceService.list());
    }

    @ApiOperation(value = "停车位列表2", notes = "停车位列表2", response = Order.class)
    @GetMapping("/{id}")
    @SaCheckPermission("parkingSpace.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(parkingSpaceService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Order.class)
    @GetMapping("/page")
    @SaCheckPermission("parkingSpace.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ParkingSpace> queryWrapper = new QueryWrapper<ParkingSpace>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(parkingSpaceService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @ApiOperation(value = "导出", notes = "导出", response = Order.class)
    @GetMapping("/export")
    @SaCheckPermission("parkingSpace.export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<ParkingSpace> list = parkingSpaceService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("ParkingSpace信息表", "UTF-8");
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
    @ApiOperation(value = "导入", notes = "导入", response = Order.class)
    @PostMapping("/import")
    @SaCheckPermission("parkingSpace.import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<ParkingSpace> list = reader.readAll(ParkingSpace.class);

        parkingSpaceService.saveBatch(list);
        return Result.success("cg");
    }

}
