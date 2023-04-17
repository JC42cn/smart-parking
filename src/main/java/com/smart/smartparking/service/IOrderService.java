package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Record;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
public interface IOrderService extends IService<Order> {
    Order selectOrderPay(Long uid);

    List<Map<String, Object>> findOrderMoneyByDay(int day);

    Integer findOrderCount();
}
