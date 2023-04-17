package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.entity.Bookings;
import com.smart.smartparking.mapper.BookingsMapper;
import com.smart.smartparking.service.BookingsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-04-06
 */
@Service
public class BookingsServiceImpl extends ServiceImpl<BookingsMapper, Bookings> implements BookingsService {
    @Resource
    private BookingsMapper bookingsMapper;

    @Override
    public Bookings findBookingsByUid(Long uid) {
        QueryWrapper<Bookings> qw = new QueryWrapper<>();
        Bookings book = getOne(qw.eq("uid",uid).eq("state",0));
        return book;
    }

//    @Override
//    public Bookings findBookingsStateByUidAndPidAndPsNumber(Long uid, int pid, int psNumber) {
//        QueryWrapper<Bookings> qw = new QueryWrapper<>();
//        Bookings book = getOne(qw.eq("uid",uid).eq("state",0).eq("ps_number",psNumber).eq("pid",pid));
//        return null;
//    }

//    @Override
//    public Bookings updateBookingsStateByUid(Long uid) {
//        QueryWrapper<Bookings> qw = new QueryWrapper<>();
//        Bookings book = getOne(qw.eq("uid",uid).eq("state",0));
//        if (LocalDateTime.now().isAfter(book.getEntryTime())){
//            book.setState(3);
//        }
//        return book;
//    }



}
