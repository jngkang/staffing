package com.staffing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Constants;
import com.staffing.common.Result;
import com.staffing.entity.Role;
import com.staffing.exception.ServiceException;
import com.staffing.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Role role) {
        return Result.success(roleService.saveOrUpdate(role));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            return Result.success(roleService.removeById(id));
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "删除失败，该角色已经分配权限，请删除其权限，再进行角色删除。");
        }
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        try {
            return Result.success(roleService.removeByIds(ids));
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "删除失败，存在角色已经分配权限，请删除其权限，再进行角色删除。");
        }
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String search) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            queryWrapper.like("name", search);
        }
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

