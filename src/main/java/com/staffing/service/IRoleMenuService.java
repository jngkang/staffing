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

    int setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);

}
