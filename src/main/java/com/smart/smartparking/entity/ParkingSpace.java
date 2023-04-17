package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("parking_space")
@ApiModel(value = "ParkingSpace对象", description = "")
public class ParkingSpace implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 停车场id
    @ApiModelProperty("停车场id")
    @Alias("停车场id")
    private Integer pid;

    // 车位号
    @ApiModelProperty("车位号")
    @Alias("车位号")
    private int psNumber;

    // 车位状态
    @ApiModelProperty("车位状态 0可正常使用 1维护")
    @Alias("车位状态")
    private Integer state;


    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  1删除")
    @Alias("逻辑删除 0存在  1删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}