package com.staffing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.staffing.controller.dto.CountDto;
import com.staffing.controller.dto.EmployeeNameInfoDto;
import com.staffing.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-23
 * @description
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * @return java.util.Map<java.lang.String, java.lang.Integer> 部门名称，部门总人数
     * @description 获取每个部门的人员数量
     */
    public List<CountDto> getDeptCount();

    /**
     * @return java.util.List<com.staffing.controller.dto.CountDto>
     * @description 获取每个岗位的人员数量
     */
    public List<CountDto> getPostCount();

    /**
     * @return com.staffing.common.Result
     * @description 根据员工编号，查询所有员工姓名、角色id、角色姓名、部门编号、部门名称、岗位编号、岗位名称
     */
    public List<EmployeeNameInfoDto> findAllNameInfoByEmpno(String empno);

}
