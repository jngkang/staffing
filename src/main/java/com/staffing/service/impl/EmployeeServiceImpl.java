package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.controller.dto.CountDto;
import com.staffing.entity.Employee;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.EmployeeMapper;
import com.staffing.service.IEmployeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
