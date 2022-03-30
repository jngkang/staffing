package com.staffing.service.impl;

import com.staffing.controller.dto.VacationPageDto;
import com.staffing.entity.Vacation;
import com.staffing.mapper.VacationMapper;
import com.staffing.service.IVacationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-29
 * @description
 */
@Service
public class VacationServiceImpl extends ServiceImpl<VacationMapper, Vacation> implements IVacationService {

    @Resource
    VacationMapper vacationMapper;

    @Override
    public Boolean insert(Vacation vacation) {
        // 默认为已提交状态
        vacation.setState(0);
        return save(vacation);
    }

    @Override
    public List<VacationPageDto> page(Integer pageNum, Integer pageSize, String empno, String roleId, String deptno, String postno, String search) {
        pageNum = (pageNum - 1) * pageSize;
        search = "%" + search + "%";
        return vacationMapper.page(pageNum, pageSize, empno, roleId, deptno, postno, search);
    }

    @Override
    public Long count(String roleId, String deptno, String postno, String search) {
        search = "%" + search + "%";
        return vacationMapper.count(roleId, deptno, postno, search);
    }
}
