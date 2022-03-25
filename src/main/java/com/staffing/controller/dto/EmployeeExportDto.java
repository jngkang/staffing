package com.staffing.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author JngKang
 * @date 2022-03-25 10:22
 * @description 员工导出信息
 */
@Getter
@Setter
public class EmployeeExportDto {

    @ApiModelProperty("员工编号")
    private String empno;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private String sexName;

    @ApiModelProperty("有效证件号")
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
    private String avatarUrl;
}
