package com.staffing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Result;
import com.staffing.entity.Icon;
import com.staffing.service.IIconService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
@RestController
@RequestMapping("/icon")
public class IconController {

    @Resource
    private IIconService iconService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Icon icon) {
        return Result.success(iconService.saveOrUpdate(icon));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(iconService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(iconService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(iconService.list());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(iconService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String search) {
        QueryWrapper<Icon> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            // 检索条件
             queryWrapper.like("name", search);
        }
        return Result.success(iconService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

