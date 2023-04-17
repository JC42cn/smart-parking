package com.smart.smartparking.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.controller.domain.AliPay;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.mapper.OrderMapper;
import com.smart.smartparking.service.IOrderService;

import com.smart.smartparking.service.IParkingService;
import com.smart.smartparking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
@RequestMapping("/order")
@Api(tags = "API订单管理")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserService userService;

    @Resource
    private IParkingService parkingService;

    @ApiOperation(value = "新增订单", notes = "新增订单", response = Order.class)
    @AutoLog("新增订单")
    @PostMapping
    //@SaCheckPermission("order.add")
    public Result save(@RequestBody Order order) {
        orderService.save(order);
        return Result.success("cg");
    }

    @ApiOperation(value = "编辑订单", notes = "编辑订单", response = Order.class)
    @AutoLog("编辑订单")
    @PutMapping
    //@SaCheckPermission("order.edit")
    public Result update(@RequestBody Order order) {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNum = Math.abs(random.nextInt() % 1000000);
        order.setOid(timestamp+randomNum);
        orderService.updateById(order);
        return Result.success("cg");
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", response = Order.class)
    @AutoLog("删除订单")
    @DeleteMapping("/{id}")
    //@SaCheckPermission("order.delete")
    public Result delete(@PathVariable Integer id) {
        orderService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除订单", notes = "批量删除订单", response = Order.class)
    @AutoLog("批量删除订单")
    @PostMapping("/del/batch")
    //@SaCheckPermission("order.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        orderService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "订单列表", notes = "订单列表", response = Order.class)
    @GetMapping
    //@SaCheckPermission("order.list")
    public Result findAll() {
        return Result.success(orderService.list());
    }

    @ApiOperation(value = "订单列表2", notes = "订单列表2", response = Order.class)
    @GetMapping("/{id}")
    //@SaCheckPermission("order.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(orderService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Order.class)
    @GetMapping("/page")
    //@SaCheckPermission("order.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam String payTime,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "car_number", name).
                like(!"".equals(payTime), "pay_time", payTime);
        return Result.success(orderService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    /**
//    * 导出接口
//    */
//    @ApiOperation(value = "导出", notes = "导出", response = Order.class)
//    @GetMapping("/export")
//    @SaCheckPermission("order.export")
//    public void export(HttpServletResponse response) throws Exception {
//        // 从数据库查询出所有的数据
//        List<Order> list = orderService.list();
//        // 在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        // 设置浏览器响应的格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("Order信息表", "UTF-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//
//        ServletOutputStream out = response.getOutputStream();
//        writer.flush(out, true);
//        out.close();
//        writer.close();
//
//    }
//
//    /**
//    * excel 导入
//    * @param file
//    * @throws Exception
//    */
//    @ApiOperation(value = "导入", notes = "导入", response = Order.class)
//    @PostMapping("/import")
//    @SaCheckPermission("order.import")
//    public Result imp(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Order> list = reader.readAll(Order.class);
//
//        orderService.saveBatch(list);
//        return Result.success("cg");
//    }

    @ApiOperation(value = "支付", notes = "支付", response = Order.class)
    //@SaCheckPermission("order.apy")
    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(URLEncoder.encode(aliPay.getSubject(), "UTF-8"), aliPay.getTraceNo(), aliPay.getTotalAmount(), "");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }


    @PostMapping("/notify")  // 注意这里必须是POST接口
    public Result payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");//商户订单号
            String gmtPayment = params.get("gmt_payment");//买家付款时间
            String alipayTradeNo = params.get("trade_no");//支付宝交易凭证号
            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//
//                // 更新订单已支付
//                orderMapper.updateState(tradeNo, "已支付", gmtPayment, alipayTradeNo);
//            }
            // 更新订单已支付
            orderMapper.updateState(tradeNo, 1,gmtPayment,alipayTradeNo);
        }
        return Result.success("支付成功！请出库");
    }

    @ApiOperation(value = "订单列表uid", notes = "订单列表uid")
    @GetMapping("/findOrderByUid")
    //@SaCheckPermission("order.uidlist")
    public Result findOrderByUid(@RequestParam(required = false) Long uid,
                                 @RequestParam Integer pageNum,
                                 @RequestParam String payTime,
                                 @RequestParam Integer pageSize) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().orderByDesc("id");
        queryWrapper.eq("uid", uid);
        queryWrapper.like(!"".equals(payTime), "pay_time", payTime);
        return Result.success(orderService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @ApiOperation(value = "订单列表根据admin", notes = "订单列表根据admin")
    @GetMapping("/findOrderByAdmin")
    public Result findOrderByPid(@RequestParam(defaultValue = "") String name,
                                 @RequestParam Long uid,
                                 @RequestParam String payTime,
                                 @RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize) {
        User user = userService.findUserByUid(uid);
        String username = user.getName();
        Parking parking = parkingService.findPakringByAdmin(username);
        int pid = parking.getPid();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "car_number", name).
                like(!"".equals(payTime), "pay_time", payTime).
                eq("pid", pid);

        return Result.success(orderService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @ApiOperation(value = "", notes = "")
    @GetMapping("/findOrderMoneyByDay")
    public Result findOrderMoneyByDay(@RequestParam int day){
        return Result.success(orderService.findOrderMoneyByDay(day));
    }

    @ApiOperation(value = "", notes = "")
    @GetMapping("/findOrderCount")
    public Result findOrderCount(){
        return Result.success(orderService.findOrderCount());
    }

}

