package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.Parking;



/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
public interface IParkingService extends IService<Parking> {

    Parking select(String name);

    Integer selectParkingSpace(String name);

    Long selectParkingPid(String name);
}
