package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.Record;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
public interface RecordMapper extends BaseMapper<Record> {

    List<Map<String, Object>> getRecordCountByDayAndPid(@Param("today") String today, @Param("day") int day);

}
