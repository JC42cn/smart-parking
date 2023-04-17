package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.ParkingSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author  
 * @since 2023-02-28
 */
@Mapper
public interface ParkingSpaceMapper extends BaseMapper<ParkingSpace> {
    @Select("SELECT * FROM parking_space WHERE pid = #{pid}")
    public List<ParkingSpace> findByPid(Integer pid);

    @Update("UPDATE parking_space SET state = #{state} WHERE pid = #{pid} AND ps_number = #{psNumber}")
    public int updateState(Integer pid, Integer psNumber, int state);

    public int findParkingSpaceCountByPid(int pid);

}
