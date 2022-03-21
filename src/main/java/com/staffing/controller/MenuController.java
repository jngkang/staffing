package com.staffing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Result;
import com.staffing.entity.Menu;
import com.staffing.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        return Result.success(menuService.saveOrUpdate(menu));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(menuService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(menuService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(menuService.findMenus());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String search) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            // 检索条件
            queryWrapper.like("name", search);
        }
        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 查询所欲表的id
     */
    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getMenuId));
    }
}

