package com.staffing.service.impl;

import com.staffing.entity.Department;
import com.staffing.mapper.DepartmentMapper;
import com.staffing.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
