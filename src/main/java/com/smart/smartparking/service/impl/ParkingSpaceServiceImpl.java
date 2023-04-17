package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.entity.ParkingSpace;
import com.smart.smartparking.mapper.ParkingSpaceMapper;
import com.smart.smartparking.service.IParkingSpaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace> implements IParkingSpaceService {

    @Resource
    private ParkingSpaceMapper parkingSpaceMapper;


    @Override
    public ParkingSpace findPaekingSpace(ParkingSpace parkingSpace) {
        QueryWrapper<ParkingSpace> qw = new QueryWrapper<>();
       ParkingSpace ps = getOne(qw.eq("pid",parkingSpace.getPid()).eq("ps_number",parkingSpace.getPsNumber()));
        return ps;
    }

    @Override
    public ParkingSpace findParkingSpaceById(Integer id) {
        QueryWrapper<ParkingSpace> qw = new QueryWrapper<>();
        ParkingSpace ps = getOne(qw.eq("id",id));
        return ps;
    }

    @Override
    public ParkingSpace findParkingSpaceByPsNumber(int psNumber,int pid) {
        QueryWrapper<ParkingSpace> qw = new QueryWrapper<>();
        ParkingSpace ps = getOne(qw.eq("ps_number",psNumber).eq("pid",pid));
        return ps;
    }


    @Override
    public int findParkingSpaceCountByPid(int pid) {
        int psCount = parkingSpaceMapper.findParkingSpaceCountByPid(pid);
        return psCount;
    }
}
