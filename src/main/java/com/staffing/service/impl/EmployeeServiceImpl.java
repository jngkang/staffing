package com.staffing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.controller.dto.CountDto;
import com.staffing.controller.dto.EmployeeDto;
import com.staffing.controller.dto.EmployeeNameInfoDto;
import com.staffing.controller.dto.EmployeePasswordDto;
import com.staffing.entity.Employee;
import com.staffing.entity.Menu;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.EmployeeMapper;
import com.staffing.mapper.RoleMapper;
import com.staffing.mapper.RoleMenuMapper;
import com.staffing.service.IEmployeeService;
import com.staffing.service.IMenuService;
import com.staffing.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public EmployeeDto login(EmployeeDto employeeDto) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("empno", employeeDto.getEmpno());
        queryWrapper.eq("password", employeeDto.getPassword());
        // 方式一
//        List<User> list = list(queryWrapper);
//        return list.size() != 0;
        // 方式二
        Employee one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            if (!one.getState()) {
                throw new ServiceException(Constants.CODE_600, "用户未激活，请联系管理员。");
            }
            BeanUtil.copyProperties(one, employeeDto, true);
            String token = TokenUtils.genToken(one.getEmpno().toString(), one.getPassword());
            employeeDto.setToken(token);
            // 设置用户角色的菜单列表
            employeeDto.setMenus(getRoleMenu(employeeDto.getRoleId()));
            return employeeDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    /**
     * @param roleId 角色名称
     * @return java.util.List<com.staffing.entity.Menu>
     * @author JngKang
     * @description 根据角色ID获取角色的菜单列表
     */
    private List<Menu> getRoleMenu(Integer roleId) {
        // 根据角色名称查出角色的id
//        Integer roleId = roleMapper.selectByName(roleId);
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

    @Override
    public Boolean updatePassword(EmployeePasswordDto employeePasswordDto) {
        if (!employeePasswordDto.getNewPassword().equals(employeePasswordDto.getConfirmPassword())) {
            throw new ServiceException(Constants.CODE_600, "两次密码输入不一致");
        }
        String password = getById(employeePasswordDto.getEmpno()).getPassword();
        if (!employeePasswordDto.getPassword().equals(password)) {
            throw new ServiceException(Constants.CODE_600, "旧密码输入错误");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("empno", employeePasswordDto.getEmpno());
        Employee employee = new Employee();
        employee.setPassword(employeePasswordDto.getNewPassword());
        return update(employee, queryWrapper);
    }

    @Override
    public Boolean insert(Employee employee) {
        Employee id = getById(employee.getPostno());
        if (id != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，员工编号编号重复。");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identify_no", employee.getIdentifyNo());
        Employee identifyNo = getOne(queryWrapper);
        if (identifyNo != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，证件号重复。");
        }
        // 状态在添加时默认为false
        employee.setState(false);
        return save(employee);
    }

    @Override
    public List<CountDto> getDeptCount() {
        return employeeMapper.getDeptCount();
    }

    @Override
    public List<CountDto> getPostCount() {
        return employeeMapper.getPostCount();
    }

    @Override
    public List<EmployeeNameInfoDto> findAllNameInfoByEmpno(String empno) {
        return employeeMapper.findAllNameInfoByEmpno(empno);
    }
}
