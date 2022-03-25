package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
@Getter
@Setter
@ApiModel(value = "Employee对象", description = "员工表")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "empno")
    @ApiModelProperty("员工编号")
    private String empno;

    @ApiModelProperty("角色ID")
    @TableField(value = "role_id")
    private Integer roleId;

    @ApiModelProperty("部门编号")
    private String deptno;

    @ApiModelProperty("岗位编号")
    private String postno;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("有效证件号")
    @TableField(value = "identify_no")
    private String identifyNo;

    @ApiModelProperty("民族")
    private String nation;

    @ApiModelProperty("籍贯")
    private String province;

    @ApiModelProperty("政治面貌")
    private String political;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("现住址")
    private String address;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("账号状态")
    private Boolean state;

    @ApiModelProperty("头像地址")
    @TableField(value = "avatar_url")
    private String avatarUrl;


}
