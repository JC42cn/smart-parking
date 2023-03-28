package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.mapper.OrderMapper;
import com.smart.smartparking.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

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
    @Override
    public Order selectOrderPay(Long uid) {
        Order order = new Order();
        QueryWrapper<Order> qw = new QueryWrapper<>();
        order=getOne(qw.eq("state",0).eq("uid",uid));
        return order;
    }
}
