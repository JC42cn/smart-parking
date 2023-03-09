package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Order;
import com.smart.smartparking.entity.Record;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
public interface IOrderService extends IService<Order> {

     Order addOrder(int pid,int psid);
}
