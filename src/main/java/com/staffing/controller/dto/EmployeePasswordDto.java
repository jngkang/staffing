package com.staffing.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JngKang
 * @date 2022-03-26 15:45
 * @description 修改密码实体
 */
@Getter
@Setter
public class EmployeePasswordDto {
    /**
     * 员工ID
     */
    private String empno;
    /**
     * 旧密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 第二次新密码
     */
    private String confirmPassword;
}
