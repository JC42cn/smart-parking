package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "Record对象", description = "")
public class Record implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 记录唯一id
    @ApiModelProperty("记录唯一id")
    @Alias("记录唯一id")
    private long rid;

    // 车辆名称
    @ApiModelProperty("车牌号")
    @Alias("车辆名称")
    private String carNumber;

    // 入库时间
    @ApiModelProperty("入库时间")
    @Alias("入库时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime intoTime;



    // 出库时间
    @ApiModelProperty("出库时间")
    @Alias("出库时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime outTime;

    // 停车费用
    @ApiModelProperty("停车费用")
    @Alias("停车费用")
    private BigDecimal money;


    // 停车场id
    @ApiModelProperty("停车场id")
    @Alias("停车场id")
    private Integer pid;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  id删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    @ApiModelProperty("逻辑完成 0未完成 1完成")
    @Alias("逻辑完成 0未完成 1完成")
    private Integer state;
}