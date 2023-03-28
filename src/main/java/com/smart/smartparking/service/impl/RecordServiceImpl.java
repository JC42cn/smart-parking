package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.entity.Record;
import com.smart.smartparking.mapper.RecordMapper;
import com.smart.smartparking.service.IRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.geom.QuadCurve2D;
import java.time.LocalDateTime;
import java.util.Date;

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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Override
    public Record getByPlateNumber(String plateNumber,int pid) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        Record record = getOne(queryWrapper.eq("car_number",plateNumber).isNull("out_time").eq("state",0).eq("pid",pid));

        return record;
    }

    @Override
    public Integer selectCount(Long pid) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("into_time").isNull("out_time").eq("pid",pid);
        int count = count(queryWrapper);
        return count;
    }

}
