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
@ApiModel(value = "Suggestion对象", description = "")
public class Suggestion implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 建议唯一id
    @ApiModelProperty("建议唯一id")
    @Alias("建议唯一id")
    private Long sid;

    // 建议内容
    @ApiModelProperty("建议内容")
    @Alias("建议内容")
    private String content;

    // 用户id
    @ApiModelProperty("用户id")
    @Alias("用户id")
    private Long uid;

    // 创建时间
    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    @TableField(fill = FieldFill.INSERT)
//    @JsonDeserialize(using = LDTConfig.CmzLdtDeSerializer.class)
//    @JsonSerialize(using = LDTConfig.CmzLdtSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 用户名
    @ApiModelProperty("用户名")
    @Alias("用户名")
    private String username;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  1删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    // 回复状态
    @ApiModelProperty("回复状态")
    @Alias("回复状态")
    private String state;

    // 回复内容
    @ApiModelProperty("回复内容")
    @Alias("回复内容")
    private String reply;
}