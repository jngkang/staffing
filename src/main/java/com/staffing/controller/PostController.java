package com.staffing.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.staffing.service.IPostService;
import com.staffing.entity.Post;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private IPostService postService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Post post) {
        return Result.success(postService.saveOrUpdate(post));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(postService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(postService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(postService.list());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(postService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam String search) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            // 检索条件
            // queryWrapper.like("username", search);// 若有多个and条件直接写多条此例
            // queryWrapper.or().like("nickname", search);// 若存在or条件写此例
			//queryWrapper.and( // 添加括号，并且以and连接
			//		QueryWrapper -> QueryWrapper.like("username", search)
			//				.or().like("nickname", search)
			//				.or().like("email", search)
			//);
        }
        return Result.success(postService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

