package com.staffing.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@Getter
@Setter
@ApiModel(value = "Post对象", description = "岗位表")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("岗位编号")
    private String postno;

    @ApiModelProperty("部门编号")
    private String deptno;

    @ApiModelProperty("岗位名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;


}
