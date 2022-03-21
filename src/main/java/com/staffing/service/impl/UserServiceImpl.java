package com.staffing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.controller.dto.UserDto;
import com.staffing.controller.dto.UserPasswordDTO;
import com.staffing.entity.Menu;
import com.staffing.entity.User;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.RoleMapper;
import com.staffing.mapper.RoleMenuMapper;
import com.staffing.mapper.UserMapper;
import com.staffing.service.IMenuService;
import com.staffing.service.IUserService;
import com.staffing.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
/**
 * @author JngKang
 * @date 2022-03-02 19:26
 * @description user
 */
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDto login(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        // 方式一
//        List<User> list = list(queryWrapper);
//        return list.size() != 0;
        // 方式二
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null && one.getState()) {
            BeanUtil.copyProperties(one, userDto, true);
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
            // 设置用户角色的菜单列表
            userDto.setMenus(getRoleMenu(userDto.getRole()));
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }

    }

    @Override
    public Boolean updatePassword(UserPasswordDTO up) {
        if (!up.getNewPassword().equals(up.getConfirmPassword())) {
            throw new ServiceException(Constants.CODE_600, "两次密码输入不一致");
        }
        String password = getById(up.getId()).getPassword();
        if (!up.getPassword().equals(password)) {
            throw new ServiceException(Constants.CODE_600, "旧密码输入错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", up.getId());
        User user = new User();
        user.setPassword(up.getNewPassword());
        return update(user, queryWrapper);
    }

    /**
     * @param role 角色名称
     * @return java.util.List<com.staffing.entity.Menu>
     * @author JngKang
     * @description 根据角色名称获取角色的菜单列表
     */
    private List<Menu> getRoleMenu(String role) {
        // 根据角色名称查出角色的id
        Integer roleId = roleMapper.selectByName(role);
        // 根据角色的id查询出角色的菜单绑定
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查询出系统的所有菜单
        List<Menu> menus = menuService.findMenus();
        // 筛选当前用户角色的菜单
        ArrayList<Menu> roleMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getMenuId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getMenuId()));
        }
        return roleMenus;
    }
}
