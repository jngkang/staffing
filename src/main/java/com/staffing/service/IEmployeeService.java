package com.staffing.service;

import com.staffing.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * @param employee 待插入的数据信息
     * @return java.lang.Boolean
     * @description 插入数据
     */
    public Boolean insert(Employee employee);

}
