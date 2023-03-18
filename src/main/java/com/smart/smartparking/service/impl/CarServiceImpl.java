package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.service.ICarService;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.mapper.CarMapper;
import org.springframework.stereotype.Service;

import java.awt.geom.QuadCurve2D;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Override
    public Car selectNumber(String carNumber) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        Car car = getOne(queryWrapper.eq("car_number",carNumber));
        return car;
    }
}
