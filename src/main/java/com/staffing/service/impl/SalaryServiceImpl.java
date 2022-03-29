package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.common.Constants;
import com.staffing.controller.dto.SalaryPageDto;
import com.staffing.entity.Salary;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.SalaryMapper;
import com.staffing.service.ISalaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-26
 * @description
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

    @Resource
    private SalaryMapper salaryMapper;

    @Override
    public List<SalaryPageDto> page(Integer pageNum, Integer pageSize, String payTime, String roleId, String deptno, String postno, String search) {
        pageNum = (pageNum - 1) * pageSize;
        search = "%" + search + "%";
        return salaryMapper.page(pageNum, pageSize, payTime, roleId, deptno, postno, search);
    }

    @Override
    public List<String> getPayDate() {
        return salaryMapper.getPayDate();
    }

    @Override
    public Object insert(Salary salary) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("empno", salary.getEmpno());
        queryWrapper.eq("pay_date", salary.getPayDate());
        Salary one = getOne(queryWrapper);
        if (one != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，本月员工工资信息已经录入。");
        }
        salary.setFsalary(salary.getBase() + salary.getPerformance() + salary.getBonus() + salary.getSubsidy()
                + salary.getInsurance() + salary.getPenalty() + salary.getAbsenteeism());
        return save(salary);
    }

    @Override
    public Long count(String payDate, String roleId, String deptno, String postno, String search) {
        search = "%" + search + "%";
        return salaryMapper.count(payDate, roleId, deptno, postno, search);
    }
}
