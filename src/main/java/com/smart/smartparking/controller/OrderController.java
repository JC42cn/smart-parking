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
import com.smart.smartparking.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
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
@RequestMapping("/order")
@Api(tags = "API订单管理")
public class OrderController {

    @Resource
    private IOrderService orderService;


    @ApiOperation(value = "新增订单", notes = "新增订单", response = Order.class)
    @AutoLog("新增订单")
    @PostMapping
    @SaCheckPermission("order.add")
    public R save(@RequestBody Order order) {
        orderService.save(order);
        return R.success("cg");
    }

    @ApiOperation(value = "编辑订单", notes = "编辑订单", response = Order.class)
    @AutoLog("编辑订单")
    @PutMapping
    @SaCheckPermission("order.edit")
    public R update(@RequestBody Order order) {
        orderService.updateById(order);
        return R.success("cg");
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", response = Order.class)
    @AutoLog("删除订单")
    @DeleteMapping("/{id}")
    @SaCheckPermission("order.delete")
    public R delete(@PathVariable Integer id) {
        orderService.removeById(id);
        return R.success("cg");
    }

    @ApiOperation(value = "批量删除订单", notes = "批量删除订单", response = Order.class)
    @AutoLog("批量删除订单")
    @PostMapping("/del/batch")
    @SaCheckPermission("order.deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids) {
        orderService.removeByIds(ids);
        return R.success("cg");
    }

    @ApiOperation(value = "订单列表", notes = "订单列表", response = Order.class)
    @GetMapping
    @SaCheckPermission("order.list")
    public R findAll() {
        return R.success(orderService.list());
    }

    @ApiOperation(value = "订单列表2", notes = "订单列表2", response = Order.class)
    @GetMapping("/{id}")
    @SaCheckPermission("order.list")
    public R findOne(@PathVariable Integer id) {
        return R.success(orderService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Order.class)
    @GetMapping("/page")
    @SaCheckPermission("order.list")
    public R findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return R.success(orderService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @ApiOperation(value = "导出", notes = "导出", response = Order.class)
    @GetMapping("/export")
    @SaCheckPermission("order.export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Order> list = orderService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Order信息表", "UTF-8");
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
    @SaCheckPermission("order.import")
    public R imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Order> list = reader.readAll(Order.class);

        orderService.saveBatch(list);
        return R.success("cg");
    }

}
