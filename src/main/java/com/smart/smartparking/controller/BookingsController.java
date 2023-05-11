package com.smart.smartparking.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Bookings;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.ParkingSpace;
import com.smart.smartparking.mapper.BookingsMapper;
import com.smart.smartparking.mapper.ParkingSpaceMapper;
import com.smart.smartparking.service.BookingsService;
import com.smart.smartparking.service.IParkingService;
import com.smart.smartparking.service.IParkingSpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
* <p>
*  前端控制器
* </p>
*
* @author 
* @since 2023-04-06
*/
@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingsController {

    @Resource
    private BookingsService bookingsService;
    @Resource
    private IParkingService parkingService;
    @Resource
    private ParkingSpaceMapper parkingSpaceMapper;
    @Resource
    private BookingsMapper bookingsMapper;

    @Resource
    private IParkingSpaceService parkingSpaceService;

    @AutoLog("新增预约管理")
    @PostMapping
    public Result save(@RequestBody Bookings bookings) {
        bookingsService.save(bookings);
        return Result.success();
    }


    @AutoLog("用户新增预约管理")
    @PostMapping("/add")
    public Result addBookings(@RequestParam Long uid,
                              @RequestParam Integer psNumber,
                              @RequestParam Integer pid) {
        //查询是否已预约
        Bookings bk = bookingsService.findBookingsByUid(uid);
        if (bk!=null){
            return Result.error("800","你已经有一个预约");
        }

        ParkingSpace ps = parkingSpaceService.findParkingSpaceByPsNumber(psNumber,pid);
        if (ps==null){
            return  Result.error("没找到该停车位");
        }else {
            ps.setState(2);
            parkingSpaceService.updateById(ps);
        }

        Bookings bookings = new Bookings();
        bookings.setUid(uid);
        bookings.setPsNumber(psNumber);
        bookings.setPid(pid);
        bookings.setEntryTime(LocalDateTime.now());
        bookingsService.save(bookings);
        LocalDateTime enTime = bookings.getEntryTime();
        LocalTime localTime = enTime.toLocalTime(); // 将LocalDateTime类型转换为LocalTime类型
        LocalTime endTime = localTime.plusMinutes(30); // 加上30分钟
        return Result.success("预约成功，请在"+endTime+"之前进入停车场");
    }

    @AutoLog("编辑预约管理")
    @PutMapping
    public Result update(@RequestBody Bookings bookings) {
        bookingsService.updateById(bookings);
        return Result.success();
    }
    @AutoLog("用户取消预约")
    @PostMapping("/cancel")
    public Result cancel(@RequestParam Long uid,
                         @RequestParam Integer psNumber,
                         @RequestParam Integer pid) {
        Bookings bk = bookingsService.findBookingsByUid(uid);
        if (bk!=null){
            bk.setState(1);
            ParkingSpace ps = parkingSpaceService.findParkingSpaceByPsNumber(psNumber,pid);
            if (ps==null){
                return  Result.error("没找到该停车位");
            }else {
                ps.setState(0);
                parkingSpaceService.updateById(ps);
            }
            bookingsService.updateById(bk);
            return Result.success("取消成功");
        }

        return Result.error("取消失败");
    }

    @AutoLog("用户查看剩余时间")
    @PostMapping("/time")
    public Result time(@RequestParam Long uid) {


        Bookings bk = bookingsService.findBookingsByUid(uid);
        if (bk==null){
            return  Result.error("没找到预约");
        }
        LocalDateTime enTime = bk.getEntryTime();
        LocalTime localTime = enTime.toLocalTime(); // 将LocalDateTime类型转换为LocalTime类型
        LocalTime endTime = localTime.plusMinutes(30); // 加上30分钟

        return Result.success(endTime);
    }


    @AutoLog("删除预约管理")
    @DeleteMapping("/{id}")

    public Result delete(@PathVariable Integer id) {
        bookingsService.removeById(id);
        return Result.success();
    }

    @AutoLog("批量删除预约管理")
    @PostMapping("/del/batch")

    public Result deleteBatch(@RequestBody List<Integer> ids) {
        bookingsService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping

    public Result findAll() {
        return Result.success(bookingsService.list());
    }

    @GetMapping("/{id}")

    public Result findOne(@PathVariable Integer id) {
        return Result.success(bookingsService.getById(id));
    }

    @GetMapping("/page")

    public Result findPage(@RequestParam int pid,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Bookings> queryWrapper = new QueryWrapper<Bookings>().orderByDesc("id");
        if (pid != 0 ){
            queryWrapper.eq("pid",pid);
        }
        return Result.success(bookingsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/findBookingsStateList")

    public Result findBookingsStateList( @RequestParam String entryTime,
                                         @RequestParam int pid,
                                         @RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize){
        QueryWrapper<Bookings> queryWrapper = new QueryWrapper<Bookings>().orderByDesc("id");
        if (pid != 0 ){
            queryWrapper.like(!"".equals(entryTime), "entry_time", entryTime).eq("pid",pid);
        }
        return Result.success(bookingsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    @Scheduled(fixedRate = 1800000) // 30 minutes
   @Scheduled(fixedRate = 5000)
    @GetMapping("/checkBookingExpiration")
    public void checkBookingExpiration() {
        List<ParkingSpace> parkingSpaces = parkingSpaceService.list();
        for (ParkingSpace parkingSpace : parkingSpaces) {
            List<Bookings> bookings = bookingsMapper.findBooked(parkingSpace.getPid(), parkingSpace.getPsNumber());
            for (Bookings booking : bookings) {
                LocalDateTime now = LocalDateTime.now();
                long diffInMillies = ChronoUnit.MILLIS.between(booking.getEntryTime(), now);
                if (diffInMillies >= 1800000) {
                    parkingSpaceMapper.updateState(parkingSpace.getPid(), parkingSpace.getPsNumber(), 0);
                    bookingsMapper.updateState(booking.getUid(), 3);
                }
            }
        }
    }

    @GetMapping("/findUserBookings")
    public Result findUserBokings(@RequestParam int pid,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                                  @RequestParam long uid) {

        QueryWrapper<Bookings> queryWrapper = new QueryWrapper<Bookings>().orderByDesc("id");
        if (pid != 0 ){
            Parking parking = parkingService.selectParkingByPid(pid);
            String name = parking.getName();
            queryWrapper.eq("pid",pid).eq("uid",uid);
        }else{
            queryWrapper.eq("uid",uid);
        }
        return Result.success(bookingsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


}
