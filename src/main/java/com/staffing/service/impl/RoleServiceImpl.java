package com.staffing.service.impl;

import com.staffing.entity.Role;
import com.staffing.mapper.RoleMapper;
import com.staffing.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
