package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.Parking;

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
public interface IParkingService extends IService<Parking> {

    Parking select(String name);

    Integer selectParkingSpaceCount(String name);

    Integer selectParkingPid(String name);

    Parking selectParkingByPid(int pid);

    Parking findPakringByAdmin(String admin);

    Map<String, Object> findParkingNumber();



}
