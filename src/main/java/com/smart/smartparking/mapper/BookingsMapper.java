package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.Bookings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface BookingsMapper extends BaseMapper<Bookings> {
    @Select("SELECT * FROM bookings WHERE pid = #{pid} AND ps_number = #{psNumber} AND state = 0")
    public List<Bookings> findBooked(Integer pid, Integer psNumber);

    @Update("UPDATE bookings SET state = #{state} WHERE uid = #{uid} AND state =0")
    public int updateState(Long uid, int state);

}
