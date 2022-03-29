package com.staffing.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Constants;
import com.staffing.common.Result;
import com.staffing.entity.Department;
import com.staffing.exception.ServiceException;
import com.staffing.service.IDepartmentService;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    /**
     * 修改数据
     */
    @PostMapping
    public Result save(@RequestBody Department department) {
        return Result.success(departmentService.saveOrUpdate(department));
    }

    /**
     * 新增数据
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Department department) {
        return Result.success(departmentService.insert(department));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        return Result.success(departmentService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<String> ids) {
        return Result.success(departmentService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(departmentService.list());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(departmentService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String search) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (!"".equals(search)) {
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("deptno", search)
                            .or().like("name", search)
                            .or().like("location", search)
            );
        }
        return Result.success(departmentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * @param response response
     * @author JngKang
     * @description 导出数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Department> list = null;
        List<String> ids = new ArrayList<>();
        // 从数据库查询出所有的数据
        if (ids.size() == 0) {
            list = departmentService.list();
        } else {
            list = departmentService.listByIds(ids);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("deptno", "部门编号");
        writer.addHeaderAlias("name", "部门名称");
        writer.addHeaderAlias("location", "部门地址");
        writer.addHeaderAlias("description", "描述");

        //水平中间对齐，垂直中间对齐
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //列宽自适应
        writer.autoSizeColumnAll();
        Sheet sheet = writer.getSheet();
        //默认取第一行数据，解决中文自适应宽度不足问题
        if (sheet != null && sheet.getRow(1) != null) {
            int physicalNumberOfCells = sheet.getRow(1).getPhysicalNumberOfCells();
            for (int i = 0; i < physicalNumberOfCells; i++) {
                // 调整每一列宽度
                sheet.autoSizeColumn((short) i);
                // 解决自动设置列宽中文失效的问题
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }
        }

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("部门信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * @param response response
     * @author JngKang
     * @description 导出导入所使用的模板
     */
    @GetMapping("/exporttemp")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //设置列宽
        writer.setColumnWidth(0, 30);
        writer.setColumnWidth(1, 30);
        writer.setColumnWidth(2, 30);
        writer.setColumnWidth(3, 60);

        //水平中间对齐，垂直中间对齐
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        // 填充数据
        String attention = "注意：";
        String attention1 = "1、带*的为必填项。";
        String attention2 = "2、部门编号的长度：3 ≤ len ≤ 6。";
        String attention3 = "3、部门编号、部门名称均不可重复（与包括已添加的数据）。";
        writer.merge(0, 0, 0, 3, attention, true);
        writer.merge(1, 1, 0, 3, attention1, true);
        writer.merge(2, 2, 0, 3, attention2, true);
        writer.merge(3, 3, 0, 3, attention3, true);
        writer.writeCellValue(0, 4, "*部门编号");
        writer.writeCellValue(1, 4, "*部门名称");
        writer.writeCellValue(2, 4, "*部门地址");
        writer.writeCellValue(3, 4, "描述");

        // 样式：字体红色，加粗，灰色背景
        CellStyle cellStyle = writer.createCellStyle();
        // 设置字体样式
        Font font = writer.createFont();
        // 红色字体
        font.setColor(Font.COLOR_RED);
        // 字体加粗
        font.setBold(true);
        cellStyle.setFont(font);
        // 设置灰色背景
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置剧中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 样式：字体加粗，灰色背景
        CellStyle cellStyle1 = writer.createCellStyle();
        Font font1 = writer.createFont();
        font1.setBold(true);
        cellStyle1.setFont(font1);
        cellStyle1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                writer.setStyle(cellStyle, i, j);
            }
        }
        for (int i = 0; i < 3; i++) {
            writer.setStyle(cellStyle1, i, 4);
        }
        writer.setStyle(cellStyle1, 3, 4);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("部门信息批量导入模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();

        //关闭writer，释放内存
        writer.close();
    }

    /**
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 导入
     */
    @Transactional
    @PostMapping("/import")
    public Result imp(@RequestParam MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(5);
        List<Department> depts = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Department dept = new Department();
            try {
                String deptno = row.get(0).toString();
                if (!(deptno.length() >= 3 && deptno.length() <= 6)) {
                    throw new ServiceException(Constants.CODE_600, "导入失败，部门编号长度在3到6个字符。");
                }
                dept.setDeptno(deptno);
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，部门编号不能为空。");
            }
            try {
                String name = row.get(1).toString();
                if (!(name.length() >= 2 && name.length() <= 160)) {
                    throw new ServiceException(Constants.CODE_600, "导入失败，部门名称长度在2到20个字符。");
                }
                dept.setName(name);
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，部门名称不能为空。");
            }
            try {
                String location = row.get(2).toString();
                if (!(location.length() >= 2 && location.length() <= 255)) {
                    throw new ServiceException(Constants.CODE_600, "导入失败，部门地址长度在2到255个字符。");
                }
                dept.setLocation(location);
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，部门地址不能为空。");
            }
            try {
                dept.setDescription(row.get(3).toString());
            } catch (Exception e) {
                dept.setDescription("");
            }
            depts.add(dept);
        }
        try {
            departmentService.saveBatch(depts);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "导入失败，部门编号或部门名称重复。");
        }
        return Result.success(true);
    }
}

