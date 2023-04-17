package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.Record;

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
public interface IRecordService extends IService<Record> {
    Record getByPlateNumber(String plateNumber,int pid);

    List<Map<String, Object>> findRecordCountByDay(String today, int day);

    Integer findRecordCount();

//    Integer findRecordCountByDay(String today,int day);

}
