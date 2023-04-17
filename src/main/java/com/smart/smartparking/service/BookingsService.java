package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.Bookings;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-04-06
 */
public interface BookingsService extends IService<Bookings> {

    Bookings findBookingsByUid(Long uid);

//    Bookings findBookingsStateByUidAndPidAndPsNumber(Long uid,int pid,int psNumber);

//    Bookings updateBookingsStateByUid(Long uid);


}
