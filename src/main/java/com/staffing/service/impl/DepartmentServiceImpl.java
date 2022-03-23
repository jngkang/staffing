package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.entity.Department;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.DepartmentMapper;
import com.staffing.service.IDepartmentService;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Override
    public boolean insert(Department department) {
        Department dept = getById(department.getDeptno());
        if (dept != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，部门编号重复。");
        }
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", department.getName());
        Department one = getOne(queryWrapper);
        if (one != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，部门名称重复。");
        }
        return save(department);
    }

    @Override
    public String getDeptnoByName(String name) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Department one = getOne(queryWrapper);
        if (one == null) {
            throw new ServiceException(Constants.CODE_600, "部门不存在。");
        }
        return one.getDeptno();
    }
}
