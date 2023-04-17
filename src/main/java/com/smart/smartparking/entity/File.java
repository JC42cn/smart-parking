package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
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
* @author 程序员青戈
* @since 2023-02-04
*/
@Getter
@Setter
@TableName("file")
@ApiModel(value = "File对象", description = "")
public class File implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 文件名
    @ApiModelProperty("文件名")
    @Alias("文件名")
    private String name;

    // 大小
    @ApiModelProperty("大小")
    @Alias("大小")
    private Long size;

    // 类型
    @ApiModelProperty("类型")
    @Alias("类型")
    private String type;

    // 访问链接
    @ApiModelProperty("访问链接")
    @Alias("访问链接")
    private String url;

    // 文件摘要
    @ApiModelProperty("文件摘要")
    @Alias("文件摘要")
    private String md5;

    // 存储地址
    @ApiModelProperty("存储地址")
    @Alias("存储地址")
    private String location;

    // 软删除
    @ApiModelProperty("逻辑删除")
    @Alias("逻辑删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

}