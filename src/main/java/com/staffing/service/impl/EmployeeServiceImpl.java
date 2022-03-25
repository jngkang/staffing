package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.entity.Employee;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.EmployeeMapper;
import com.staffing.service.IEmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

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
}
