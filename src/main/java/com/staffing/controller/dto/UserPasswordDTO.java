package com.staffing.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author JngKang
 * @date 2022-03-18 14:10
 * @description 封装用户修改密码的信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserPasswordDTO {
    private Integer id;
    private String password;
    private String newPassword;
    private String confirmPassword;
}
