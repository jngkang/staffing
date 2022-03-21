package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
@Getter
@Setter
@TableName("sys_icon")
@ApiModel(value = "Icon对象", description = "")
public class Icon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "icon_id", type = IdType.AUTO)
    private Integer iconId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("icon值")
    private String icon;

}
