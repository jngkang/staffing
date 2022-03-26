package com.staffing.controller.dto;

import com.staffing.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-26 15:42
 * @description 登录数据实体，接受前端登录请求的参数
 */
@Getter
@Setter
public class EmployeeDto {
    /**
     * 员工ID
     */
    private String empno;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像的URL
     */
    private String avatarUrl;
    /**
     * token
     */
    private String token;
    /**
     * 菜单列表
     */
    private List<Menu> menus;
}
