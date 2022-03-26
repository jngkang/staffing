package com.staffing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.staffing.controller.dto.CountDto;
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
     * @return java.util.Map<java.lang.String,java.lang.Integer> 部门名称，部门总人数
     * @description 获取每个部门的人员数量
     */
    public List<CountDto> getDeptCount();

    public List<CountDto> getPostCount();

}
