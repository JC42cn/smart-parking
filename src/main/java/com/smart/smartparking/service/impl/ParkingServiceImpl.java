package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.mapper.ParkingMapper;
import com.smart.smartparking.service.IParkingService;
import org.springframework.stereotype.Service;

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
@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements IParkingService {

    @Override
    public Parking select(String name) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        Parking parking = new Parking();
        parking = getOne(queryWrapper.eq("name",name));
        return parking;
    }

    @Override
    public Integer selectParkingSpace(String name) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        Parking parking =getOne(queryWrapper.eq("name",name));
        int number = parking.getParkingSpaceNumber();
        return number;
    }

    @Override
    public Integer selectParkingPid(String name) {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        Parking parking =getOne(queryWrapper.eq("name",name));
        int pid = parking.getPid();
        return pid;
    }


}
