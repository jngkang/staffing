package com.staffing.controller;


import com.staffing.common.Result;
import com.staffing.service.IRoleMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {

    @Resource
    private IRoleMenuService roleMenuService;

    /**
     * @param roleId  角色ID
     * @param menuIds 菜单的ID数据
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 绑定角色和菜单的关系。先根据roleIde删除数据库所有的绑定，再根据menuIds添加绑定
     */
    @PostMapping("/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        return Result.success(roleMenuService.setRoleMenu(roleId, menuIds));
    }

    /**
     * @param roleId 角色ID
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 根据角色ID查询所绑定的菜单ID数组
     */
    @GetMapping("/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success(roleMenuService.getRoleMenu(roleId));
    }

}

