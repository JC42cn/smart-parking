package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.smart.smartparking.common.LDTConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* 
* </p>
*
* @author  
* @since 2023-02-28
*/
@Getter
@Setter
@ApiModel(value = "Order对象", description = "")
@TableName("orders")
public class Order implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 订单号
    @ApiModelProperty("订单号")
    @Alias("订单号")
    private Long oid;

    // 创建时间
    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    @TableField(fill = FieldFill.INSERT)
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    // 车位id
    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer uid;

    // 停车场id
    @ApiModelProperty("停车场id")
    @Alias("停车场id")
    private Integer pid;

    // 支付状态
    @ApiModelProperty("支付状态")
    @Alias("支付状态")
    private Integer state;

    // 支付时间
    @ApiModelProperty("支付时间")
    @Alias("支付时间")
    private String payTime;

    // 支付宝交易号
    @ApiModelProperty("支付宝交易号")
    @Alias("支付宝交易号")
    private String alipayNo;

    // 停车费用
    @ApiModelProperty("停车费用")
    @Alias("停车费用")
    private BigDecimal money;
    // 停车费用

    @ApiModelProperty("车牌号")
    @Alias("车牌号")
    private String carNumber;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  id删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;
}