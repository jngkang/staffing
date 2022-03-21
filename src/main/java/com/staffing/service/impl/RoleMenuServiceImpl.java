package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.entity.Menu;
import com.staffing.entity.RoleMenu;
import com.staffing.mapper.RoleMenuMapper;
import com.staffing.service.IMenuService;
import com.staffing.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Resource
    IMenuService menuService;

    // @Transactional  注解：事务注解，当数据操作发生异常时，会自动回滚
    @Transactional
    @Override
    public int setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 1、先删除roleId已经绑定的菜单
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(queryWrapper);

        // 2、再添加前端传过来的菜单id数组绑定到当前的角色id上
        for (Integer menuId : menuIds) {
            // 查询出该菜单ID的信息
            Menu menu = menuService.getById(menuId);
            // 判断该菜单是否为二级菜单，并且该菜单menuId数组中没有父级的id，则进行添加
            if (menu.getPid() != null && !menuIds.contains(menu.getPid())) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
            }
            // 若父级id已经添加则直接添加
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }

        return 0;
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }

}
