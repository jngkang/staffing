package com.staffing.service;

import com.staffing.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
public interface IRoleService extends IService<Role> {
    /**
     * @param roleName 角色名称
     * @return java.lang.Integer
     * @description 根据角色名称查询角色id
     */
    public Integer getRoleIdByName(String roleName);

}
