package com.staffing.service;

import com.staffing.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * @param roleId 角色id
     * @param menuIds 待绑定的菜单
     * @return int
     * @description 保存角色菜单，思路：先删除角色相关所有数据，然后再进行添加
     */
    int setRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * @param roleId 角色id
     * @return java.util.List<java.lang.Integer>
     * @description 获取角色所绑定的id
     */
    List<Integer> getRoleMenu(Integer roleId);
}
