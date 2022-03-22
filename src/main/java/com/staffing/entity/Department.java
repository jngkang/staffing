package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@Getter
@Setter
@ApiModel(value = "Department对象", description = "部门表")
@TableName("department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门编号")
    @TableId(value = "deptno")
    private String deptno;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门地址")
    private String location;

    @ApiModelProperty("描述")
    private String description;


}
