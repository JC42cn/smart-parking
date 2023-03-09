package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Data
@ApiModel(value = "Operation对象", description = "")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 菜单名
    @ApiModelProperty("菜单名")
    @Alias("菜单名")
    private String name;

    // 路径
    @ApiModelProperty("路径")
    @Alias("路径")
    private String path;

    // 图标
    @ApiModelProperty("图标")
    @Alias("图标")
    private String icon;

    // 页面路径
    @ApiModelProperty("页面路径")
    @Alias("页面路径")
    private String page;

    // 父级菜单ID
    @ApiModelProperty("父级菜单ID")
    @Alias("父级菜单ID")
    private Integer pId;

    // 逻辑删除默认为0
    @ApiModelProperty("逻辑删除默认为0")
    @Alias("逻辑删除默认为0")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    // 操作
    @ApiModelProperty("操作")
    @Alias("操作")
    private String auth;

    private Integer type;

    private Integer orders;
    @TableField(exist = false)
    private List<Permission> children;
}
