package com.staffing.mapper;

import com.staffing.controller.dto.VacationPageDto;
import com.staffing.entity.Vacation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-29
 * @description
 */
@Mapper
public interface VacationMapper extends BaseMapper<Vacation> {

    /**
     * @param pageNum  页码
     * @param pageSize 一页的数据量
     * @param empno    员工id
     * @param roleId   角色id
     * @param deptno   部门编号
     * @param postno   岗位编号
     * @param search   搜索的内容
     * @return java.util.List<com.staffing.entity.Vacation>
     * @description 分页查询
     */
    List<VacationPageDto> page(Integer pageNum, Integer pageSize, String empno, String roleId, String deptno, String postno, String search);

    /**
     * @param roleId 角色id
     * @param deptno 部门编号
     * @param postno 岗位编号
     * @param search 搜索的内容
     * @return java.lang.Long
     * @description 统计指定条件下数据的个数
     */
    Long count(String roleId, String deptno, String postno, String search);
}
