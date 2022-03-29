package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.controller.dto.VacationPageDto;
import com.staffing.entity.Vacation;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-29
 * @description
 */
public interface IVacationService extends IService<Vacation> {

    /**
     * @param vacation 待插入的数据
     * @return java.lang.Boolean
     * @description 插入数据
     */
    Boolean insert(Vacation vacation);

    /**
     * @param pageNum  页码
     * @param pageSize 一页的数据量
     * @param roleId   角色id
     * @param deptno   部门编号
     * @param postno   岗位编号
     * @param search   搜索的内容
     * @return java.lang.Object
     * @description 分页查询
     */
    List<VacationPageDto> page(Integer pageNum, Integer pageSize, String roleId, String deptno, String postno, String search);

    /**
     * @param roleId   角色id
     * @param deptno   部门编号
     * @param postno   岗位编号
     * @param search   搜索的内容
     * @return java.lang.Long
     * @description 统计指定条件下数据的个数
     */
    Long count(String roleId, String deptno, String postno, String search);
}
