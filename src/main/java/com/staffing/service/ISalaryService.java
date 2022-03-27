package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.controller.dto.SalaryPageDto;
import com.staffing.entity.Salary;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-26
 * @description
 */
public interface ISalaryService extends IService<Salary> {

    /**
     * @return java.util.List<com.staffing.controller.dto.SalaryDto>
     * @description 分页查询
     */
    public List<SalaryPageDto> page(Integer pageNum, Integer pageSize, String payTime, String roleId, String deptno, String postno, String search);

    /**
     * @return java.util.List<java.lang.String>
     * @description 获取发资日期
     */
    public List<String> getPayDate();

    /**
     * @param salary 待添加数据
     * @return java.lang.Object
     * @description 添加数据
     */
    Object insert(Salary salary);

    Long count(String payDate, String roleId, String deptno, String postno, String search);
}
