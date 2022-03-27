package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.controller.dto.CountDto;
import com.staffing.controller.dto.EmployeeDto;
import com.staffing.controller.dto.EmployeeNameInfoDto;
import com.staffing.controller.dto.EmployeePasswordDto;
import com.staffing.entity.Employee;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * @param employeeDto 登录信息
     * @return com.staffing.controller.dto.UserDto
     * @description 登录验证
     */
    EmployeeDto login(EmployeeDto employeeDto);

    /**
     * @param employeePasswordDto 密码相关信息
     * @return java.lang.Boolean
     * @description 修改密码
     */
    Boolean updatePassword(EmployeePasswordDto employeePasswordDto);

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

    /**
     * @return com.staffing.common.Result
     * @description 根据员工编号，查询所有员工姓名、角色id、角色姓名、部门编号、部门名称、岗位编号、岗位名称
     */
    public List<EmployeeNameInfoDto> findAllNameInfoByEmpno(String empno);

}
