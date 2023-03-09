package com.smart.smartparking.entity;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;


@Getter
@Setter
@Data
@ApiModel(value = "Roles对象", description = "")
public class Role implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    // 名字
    @ApiModelProperty("名字")
    @Alias("名字")
    private String name;

    // 逻辑删除
    @ApiModelProperty("逻辑删除")
    @Alias("逻辑删除")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    // 标志 USER为普通用户 ADMIN为管理人员
    @ApiModelProperty("标志 USER为普通用户 ADMIN为管理人员")
    @Alias("标志 USER为普通用户 ADMIN为管理人员")
    private String flag;

}
