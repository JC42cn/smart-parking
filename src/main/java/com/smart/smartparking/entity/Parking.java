package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.smartparking.common.LDTConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@ApiModel(value = "Parking对象", description = "")
public class Parking implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 停车场唯一id
    @ApiModelProperty("停车场唯一id")
    @Alias("停车场唯一id")
    private Integer pid;

    // 名称
    @ApiModelProperty("名称")
    @Alias("名称")
    private String name;

    // 车位数量
    @ApiModelProperty("车位数量")
    @Alias("车位数量")
    private Integer parkingSpaceNumber;

    // 免费时长
    @ApiModelProperty("免费时长")
    @Alias("免费时长")
    private Integer freeTime;

    // 创建时间
    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    @TableField(fill = FieldFill.INSERT)
//    @JsonDeserialize(using = LDTConfig.CmzLdtDeSerializer.class)
//    @JsonSerialize(using = LDTConfig.CmzLdtSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 更新时间
    @ApiModelProperty("更新时间")
    @Alias("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @JsonDeserialize(using = LDTConfig.CmzLdtDeSerializer.class)
//    @JsonSerialize(using = LDTConfig.CmzLdtSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 计时单元
    @ApiModelProperty("计时单元")
    @Alias("计时单元")
    private Integer timeUnit;

    // 单元收费
    @ApiModelProperty("单元收费")
    @Alias("单元收费")
    private BigDecimal unitCost;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  id删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;
}