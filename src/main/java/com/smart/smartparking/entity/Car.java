package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@ApiModel(value = "Car对象", description = "")
public class Car implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 车辆id
    @ApiModelProperty("车辆id")
    @Alias("车辆id")
    private Integer cid;

    // 车辆名称
    @ApiModelProperty("车辆名称")
    @Alias("车辆名称")
    private String carName;

    // 车牌号
    @ApiModelProperty("车牌号")
    @Alias("车牌号")
    private String number;

    // 逻辑删除
    @ApiModelProperty("逻辑删除")
    @Alias("逻辑删除")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    // 用户id
    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Integer uid;
}