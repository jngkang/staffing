package com.staffing.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.staffing.common.Result;
import com.staffing.entity.Vacation;
import com.staffing.service.IVacationService;
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
 * @date 2022-03-29
 * @description
 */
@RestController
@RequestMapping("/vacation")
public class VacationController {

    @Resource
    private IVacationService vacationService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Vacation vacation) {
        return Result.success(vacationService.saveOrUpdate(vacation));
    }

    /**
     * 新增数据
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Vacation vacation) {
        return Result.success(vacationService.insert(vacation));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(vacationService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(vacationService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(vacationService.list());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(vacationService.getById(id));
    }

    /**
     * 查询数据的总数
     */
    @GetMapping("/count")
    public Result count(@RequestParam String roleId,
                        @RequestParam String deptno,
                        @RequestParam String postno,
                        @RequestParam String search) {
        return Result.success(vacationService.count(roleId, deptno, postno, search));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String roleId,
                           @RequestParam String deptno,
                           @RequestParam String postno,
                           @RequestParam String search) {
        QueryWrapper<Vacation> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            queryWrapper.and( // 添加括号，并且以and连接
                    QueryWrapper -> QueryWrapper.like("empno", search)
                            .or().like("name", search)
            );
        }
        if (!"".equals(roleId)) {
            queryWrapper.like("role_id", roleId);
        }
        if (!"".equals(deptno)) {
            queryWrapper.like("deptno", deptno);
        }
        if (!"".equals(postno)) {
            queryWrapper.like("postno", postno);
        }
        return Result.success(vacationService.page(pageNum, pageSize, roleId, deptno, postno, search));
    }
}

