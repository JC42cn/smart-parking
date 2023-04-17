package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.mapper.OrderMapper;
import com.smart.smartparking.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderMapper orderMapper;
    @Override
    public Order selectOrderPay(Long uid) {
        Order order = new Order();
        QueryWrapper<Order> qw = new QueryWrapper<>();
        order=getOne(qw.eq("state",0).eq("uid",uid));
        return order;
    }

    @Override
    public List<Map<String, Object>> findOrderMoneyByDay(int day) {
        return orderMapper.findOrderMoneyByDay(day);
    }

    @Override
    public Integer findOrderCount() {
        QueryWrapper<Order> qw = new QueryWrapper<>();
        int orderCount =  count(qw);
        return orderCount;
    }
}
