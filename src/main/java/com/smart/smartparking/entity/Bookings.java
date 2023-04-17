package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
* 
* </p>
*
* @author 
* @since 2023-04-06
*/
@Data
@ApiModel(value = "Bookings对象", description = "")
public class Bookings implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 用户ID
    @ApiModelProperty("用户ID")
    @Alias("用户ID")
    private Long uid;

    // 停车场ID
    @ApiModelProperty("停车场ID")
    @Alias("停车场ID")
    private Integer pid;

    // 车位号
    @ApiModelProperty("车位号")
    @Alias("车位号")
    private Integer psNumber;


    // 预约时间
    @ApiModelProperty("预约时间")
    @Alias("预约时间")
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    // 预约状态，例如：已预约、已取消、已完成等
    @ApiModelProperty("预约状态，例如：已预约、已取消、已完成等")
    @Alias("预约状态，例如：0：已预约、1：已取消、2：已完成,3:过期")
    private int state;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  id删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

}