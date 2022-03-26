package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.controller.dto.CountDto;
import com.staffing.entity.Employee;

import java.util.List;

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

    /**
     * @return java.util.List<com.staffing.controller.dto.CountDto>
     * @description 查询部门名称和该部门的人员总数
     */
    public List<CountDto> getDeptCount();

    /**
     * @param
     * @return java.util.List<com.staffing.controller.dto.CountDto>
     * @description 查询岗位名称和该岗位的人员总数
     */
    public List<CountDto> getPostCount();

}
