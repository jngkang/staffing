package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.entity.Role;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.RoleMapper;
import com.staffing.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Integer getRoleIdByName(String roleName) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", roleName);
        Role one = getOne(queryWrapper);
        if (one == null) {
            throw new ServiceException(Constants.CODE_600, "角色不存在。");
        }
        return one.getRoleId();
    }

}
