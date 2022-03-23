package com.staffing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.staffing.entity.Department;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
public interface IDepartmentService extends IService<Department> {
    /**
     * @param department 需要插入的数据
     * @return boolean
     * @description 插入数据
     */
    public boolean insert(Department department);

    /**
     * @param name 部门名称
     * @return java.lang.String
     * @description 根据部门名称获取部门编号
     */
    public String getDeptnoByName(String name);
}
