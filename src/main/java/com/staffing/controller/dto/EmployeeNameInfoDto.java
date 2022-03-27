package com.staffing.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JngKang
 * @date 2022-03-27 16:24
 * @description 员工所有名称实体
 */
@Getter
@Setter
public class EmployeeNameInfoDto {
    private String empno;
    private String empName;
    private String roleId;
    private String roleName;
    private String deptno;
    private String deptName;
    private String postno;
    private String postName;
}
