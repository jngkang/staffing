package com.staffing.controller.dto;

import com.staffing.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-02-28 17:08
 * @description 接受前端登录请求的参数
 */
@Data
public class UserDto {
    private String id;
    private String username;
    private String nickname;
    private String password;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
