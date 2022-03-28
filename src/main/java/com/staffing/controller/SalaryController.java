package com.staffing.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.staffing.common.Constants;
import com.staffing.common.Result;
import com.staffing.controller.dto.SalaryExportDto;
import com.staffing.entity.Employee;
import com.staffing.entity.Salary;
import com.staffing.exception.ServiceException;
import com.staffing.service.IEmployeeService;
import com.staffing.service.ISalaryService;
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
 * @date 2022-03-26
 * @description
 */
@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Resource
    private ISalaryService salaryService;

    @Resource
    private IEmployeeService employeeService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Salary salary) {
        return Result.success(salaryService.saveOrUpdate(salary));
    }

    /**
     * 新增数据
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Salary salary) {
        return Result.success(salaryService.insert(salary));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        return Result.success(salaryService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<String> ids) {
        return Result.success(salaryService.removeByIds(ids));
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{empno}")
    public Result findOne(@PathVariable String empno) {
        return Result.success(salaryService.getById(empno));
    }

    /**
     * 查询数据的总数
     */
    @GetMapping("/count")
    public Result count(@RequestParam String payDate, @RequestParam String roleId, @RequestParam String deptno, @RequestParam String postno, @RequestParam String search) {
        return Result.success(salaryService.count(payDate, roleId, deptno, postno, search));
    }

    /**
     * 查询所有的发资日期
     */
    @GetMapping("/paydate")
    public Result getPayDate() {
        return Result.success(salaryService.getPayDate());
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String payDate, @RequestParam String roleId, @RequestParam String deptno, @RequestParam String postno, @RequestParam String search) {
        return Result.success(salaryService.page(pageNum, pageSize, payDate, roleId, deptno, postno, search));
    }

    /**
     * @param response response
     * @author JngKang
     * @description 导出数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Salary> list = null;
        List<String> ids = new ArrayList<>();
        // 从数据库查询出所有的数据
        if (ids.size() == 0) {
            list = salaryService.list();
        } else {
            list = salaryService.listByIds(ids);
        }
        // 根据从数据库中查询数的数据，将部门编号替换为部门名称
        List<SalaryExportDto> listDto = new ArrayList<>();
        for (Salary salary : list) {
            SalaryExportDto salaryExportDto = new SalaryExportDto();
            BeanUtil.copyProperties(salary, salaryExportDto, true);
            salaryExportDto.setEmpName(employeeService.findAllNameInfoByEmpno(salary.getEmpno()).get(0).getEmpName());
            salaryExportDto.setRoleName(employeeService.findAllNameInfoByEmpno(salary.getEmpno()).get(0).getRoleName());
            salaryExportDto.setDeptName(employeeService.findAllNameInfoByEmpno(salary.getEmpno()).get(0).getDeptName());
            salaryExportDto.setPostName(employeeService.findAllNameInfoByEmpno(salary.getEmpno()).get(0).getPostName());
            listDto.add(salaryExportDto);
        }

        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("payDate", "发放日期");
        writer.addHeaderAlias("empno", "员工编号");
        writer.addHeaderAlias("empName", "员工姓名");
        writer.addHeaderAlias("roleName", "角色名称");
        writer.addHeaderAlias("deptName", "部门名称");
        writer.addHeaderAlias("postName", "岗位名称");
        writer.addHeaderAlias("base", "基本工资");
        writer.addHeaderAlias("performance", "绩效工资");
        writer.addHeaderAlias("bonus", "奖金");
        writer.addHeaderAlias("subsidy", "补助");
        writer.addHeaderAlias("insurance", "保险");
        writer.addHeaderAlias("penalty", "罚款");
        writer.addHeaderAlias("absenteeism", "缺勤");
        writer.addHeaderAlias("fsalary", "实发工资");
        writer.addHeaderAlias("inputTime", "录入时间");
        writer.addHeaderAlias("remark", "备注");

        //水平中间对齐，垂直中间对齐
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(listDto, true);

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
        String fileName = URLEncoder.encode("岗位信息", "UTF-8");
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
        writer.setColumnWidth(0, 25);
        for (int i = 1; i < 9; i++) {
            writer.setColumnWidth(i, 15);
        }
        writer.setColumnWidth(9, 40);

        //水平中间对齐，垂直中间对齐
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        // 填充数据
        String attention = "注意：";
        String attention1 = "1、带*的为必填项。";
        String attention2 = "2、岗位编号的长度：3 ≤ len ≤ 18，并且不可重复（包括已添加的数据）";
        String attention3 = "3、金额相关列，不能为空，若为空则填入0。";
        writer.merge(0, 0, 0, 9, attention, true);
        writer.merge(1, 1, 0, 9, attention1, true);
        writer.merge(2, 2, 0, 9, attention2, true);
        writer.merge(3, 3, 0, 9, attention3, true);
        writer.writeCellValue(0, 4, "*发放日期(例:2020-03)");
        writer.writeCellValue(1, 4, "*员工编号");
        writer.writeCellValue(2, 4, "*基本工资");
        writer.writeCellValue(3, 4, "*绩效工资(￥)");
        writer.writeCellValue(4, 4, "*奖金(￥)");
        writer.writeCellValue(5, 4, "*补助(￥)");
        writer.writeCellValue(6, 4, "*保险(￥)");
        writer.writeCellValue(7, 4, "*罚款(￥)");
        writer.writeCellValue(8, 4, "*缺勤(￥)");
        writer.writeCellValue(9, 4, "备注");

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

        // 提示语设置样式
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                writer.setStyle(cellStyle, i, j);
            }
        }
        // 表头设置样式
        for (int i = 0; i < 10; i++) {
            writer.setStyle(cellStyle1, i, 4);
        }
        writer.setStyle(cellStyle1, 3, 4);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("岗位信息批量导入模板", "UTF-8");
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
        List<Salary> salaries = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Salary salary = new Salary();

            String payDate = "";
            try {
                payDate = row.get(0).toString();

            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，发放工资日期不能为空");
            }
            String payDateReg = "\\d{4}-((0([1-9]))|(1(0|1|2)))$";
            if (!payDate.matches(payDateReg)) {
                throw new ServiceException(Constants.CODE_600, "导入失败，发放工资日期格式有误");
            }
            salary.setPayDate(payDate);


            String empno = null;
            try {
                empno = row.get(1).toString();
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，员工编号不能为空。");
            }
            if (!(empno.length() >= 3 && empno.length() <= 18)) {
                throw new ServiceException(Constants.CODE_600, "导入失败，员工编号长度在3到18个字符。");
            }
            Employee byId = employeeService.getById(empno);
            if (byId == null) {
                throw new ServiceException(Constants.CODE_600, "导入失败，员工编号不存在。");
            }
            salary.setEmpno(empno);

            Float base = null;
            Float performance = null;
            Float bonus = null;
            Float subsidy = null;
            Float insurance = null;
            Float penalty = null;
            Float absenteeism = null;
            try {
                base = Float.parseFloat(row.get(2).toString());
                performance = Float.parseFloat(row.get(3).toString());
                bonus = Float.parseFloat(row.get(4).toString());
                subsidy = Float.parseFloat(row.get(5).toString());
                insurance = Float.parseFloat(row.get(6).toString());
                penalty = Float.parseFloat(row.get(7).toString());
                absenteeism = Float.parseFloat(row.get(8).toString());
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，金额格式不能为空");
            }
            String moneyReg = "(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$";
            if (!base.toString().matches(moneyReg) || !performance.toString().matches(moneyReg) || !bonus.toString().matches(moneyReg) || !subsidy.toString().matches(moneyReg)
                    || !insurance.toString().matches(moneyReg) || !penalty.toString().matches(moneyReg) || !absenteeism.toString().matches(moneyReg)) {
                throw new ServiceException(Constants.CODE_600, "导入失败，金额格式输入有误");
            }
            salary.setBase(base);
            salary.setPerformance(performance);
            salary.setBonus(bonus);
            salary.setSubsidy(subsidy);
            salary.setInsurance(insurance);
            salary.setPenalty(penalty);
            salary.setAbsenteeism(absenteeism);
            salary.setFsalary(base + performance + bonus + subsidy + insurance + penalty + absenteeism);

            try {
                salary.setRemark(row.get(9).toString());
            } catch (Exception ignored) {
                salary.setRemark("");
            }
            salaries.add(salary);
        }
        try {
            salaryService.saveBatch(salaries);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "导入失败，系统错误。");
        }
        return Result.success(true);
    }
}

