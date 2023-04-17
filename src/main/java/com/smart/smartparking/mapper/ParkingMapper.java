package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.Parking;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
@Mapper
public interface ParkingMapper extends BaseMapper<Parking> {

    Map<String, Object> findParkingNumber();

}
