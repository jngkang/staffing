package com.staffing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.staffing.controller.dto.SalaryPageDto;
import com.staffing.entity.Salary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-26
 * @description
 */
@Mapper
public interface SalaryMapper extends BaseMapper<Salary> {

    /**
     * @param pageNum 页码
     * @param pageSize 一页的数据量
     * @param payDate 发资日期
     * @param roleId 角色id
     * @param deptno 部门编号
     * @param postno 岗位编号
     * @param search 搜索的内容
     * @return java.util.List<com.staffing.controller.dto.SalaryPageDto>
     * @description 分页查询
     */
    public List<SalaryPageDto> page(Integer pageNum, Integer pageSize, String payDate, String roleId, String deptno, String postno, String search);

    /**
     * @param payDate 发资日期
     * @param roleId 角色id
     * @param deptno 部门编号
     * @param postno 岗位编号
     * @param search 搜索的内容
     * @return java.lang.Long
     * @description 统计数据数量
     */
    public Long count(String payDate, String roleId, String deptno, String postno, String search);
    
    /**
     * @return java.util.List<java.lang.String>
     * @description 获取表格中工资发放日期
     */
    public List<String> getPayDate();

}
