package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.Order;
import org.apache.ibatis.annotations.*;

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
public interface OrderMapper extends BaseMapper<Order> {
    @Update("update orders set state = #{state},pay_time = #{payTime},alipay_no=#{alipayNo} where oid=#{oid}")
    void updateState(@Param("oid") String tradeNo,@Param("state") Integer state , @Param("payTime") String gmtPayment,
                     @Param("alipayNo") String alipayTradeNo);

    List<Map<String, Object>> findOrderMoneyByDay(@Param("day") int day);

}
