package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.ParkingSpace;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
public interface IParkingSpaceService extends IService<ParkingSpace> {

    ParkingSpace findPaekingSpace( ParkingSpace parkingSpace);

    ParkingSpace findParkingSpaceById(Integer id);

    //根据psNumber和pid查找车位
    ParkingSpace findParkingSpaceByPsNumber(int psNumber , int pid);

    int findParkingSpaceCountByPid(int pid);


}
