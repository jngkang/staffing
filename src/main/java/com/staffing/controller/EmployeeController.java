package com.staffing.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Constants;
import com.staffing.common.Result;
import com.staffing.controller.dto.EmployeeDto;
import com.staffing.controller.dto.EmployeeExportDto;
import com.staffing.controller.dto.EmployeePasswordDto;
import com.staffing.entity.Employee;
import com.staffing.entity.Post;
import com.staffing.exception.ServiceException;
import com.staffing.service.IDepartmentService;
import com.staffing.service.IEmployeeService;
import com.staffing.service.IPostService;
import com.staffing.service.IRoleService;
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
 * @date 2022-03-23
 * @description
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private IEmployeeService employeeService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IDepartmentService departmentService;

    @Resource
    private IPostService postService;

    /**
     * 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody Employee employee) {
        return Result.success(employeeService.saveOrUpdate(employee));
    }

    /**
     * 新增数据
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Employee employee) {
        return Result.success(employeeService.insert(employee));
    }

    /**
     * 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        return Result.success(employeeService.removeById(id));
    }

    /**
     * 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<String> ids) {
        return Result.success(employeeService.removeByIds(ids));
    }

    /**
     * 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(employeeService.list());
    }

    /**
     * 查询员工总数
     */
    @GetMapping("/count")
    public Result findCount() {
        return Result.success(employeeService.count());
    }

    /**
     * 查询每个部门员工总数
     */
    @GetMapping("/deptcount")
    public Result findDeptCount() {
        return Result.success(employeeService.getDeptCount());
    }

    /**
     * 查询每个部门员工总数
     */
    @GetMapping("/postcount")
    public Result findPostCount() {
        return Result.success(employeeService.getPostCount());
    }

    /**
     * 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable String id) {
        return Result.success(employeeService.getById(id));
    }

    /**
     * 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String sex,
                           @RequestParam String roleId,
                           @RequestParam String deptno,
                           @RequestParam String postno,
                           @RequestParam String search) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        if (!"".equals(sex)) {
            queryWrapper.like("sex", sex);
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
        if (!"".equals(search)) {
            // 添加括号，并且以and连接
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("empno", search)
                            .or().like("name", search)
                            .or().like("phone", search)
                            .or().like("identify_no", search)
                            .or().like("address", search)
            );
        }
        return Result.success(employeeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @PostMapping("/login")
    public Result login(@RequestBody EmployeeDto employeeDto) {
        if (StrUtil.isBlank(employeeDto.getEmpno()) || StrUtil.isBlank(employeeDto.getPassword())) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(employeeService.login(employeeDto));
    }

    @PostMapping("/password")
    public Result password(@RequestBody EmployeePasswordDto employeePasswordDto) {
        return Result.success(employeeService.updatePassword(employeePasswordDto));
    }

    /**
     * @param response response
     * @author JngKang
     * @description 导出数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Employee> list = null;
        List<String> ids = new ArrayList<>();
        // 从数据库查询出所有的数据
        if (ids.size() == 0) {
            list = employeeService.list();
        } else {
            list = employeeService.listByIds(ids);
        }
        // 根据从数据库中查询数的数据，将部门编号替换为部门名称
        List<EmployeeExportDto> listDto = new ArrayList<>();
        for (Employee employee : list) {
            EmployeeExportDto employeeExportDto = new EmployeeExportDto();
            BeanUtil.copyProperties(employee, employeeExportDto, true);
            employeeExportDto.setSexName(employee.getSex() ? "男" : "女");
            employeeExportDto.setRoleName(roleService.getById(employee.getRoleId()).getName());
            employeeExportDto.setDeptName(departmentService.getById(employee.getDeptno()).getName());
            employeeExportDto.setPostName(postService.getById(employee.getPostno()).getName());
            listDto.add(employeeExportDto);
        }

        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("empno", "员工编号");
        writer.addHeaderAlias("name", "员工姓名");
        writer.addHeaderAlias("roleName", "角色");
        writer.addHeaderAlias("deptName", "部门名称");
        writer.addHeaderAlias("postName", "岗位名称");
        writer.addHeaderAlias("sexName", "性别");
        writer.addHeaderAlias("nation", "民族");
        writer.addHeaderAlias("province", "籍贯");
        writer.addHeaderAlias("political", "政治面貌");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("identifyNo", "证件号");
        writer.addHeaderAlias("address", "现住址");
        writer.addHeaderAlias("state", "账号状态");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("avatarUrl", "头像");

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
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
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
        for (int i = 0; i < 12; i++) {
            writer.setColumnWidth(i, 20);
        }
        writer.setColumnWidth(12, 60);

        //水平中间对齐，垂直中间对齐
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        // 填充数据
        String attention = "注意：";
        String attention1 = "1、带*的为必填项。";
        String attention2 = "2、员工编号的长度：6 ≤ len ≤ 18。";
        String attention3 = "3、员工编号、员工名称、员工证件号均不可重复（包括与已添加的数据）。";
        writer.merge(0, 0, 0, 12, attention, true);
        writer.merge(1, 1, 0, 12, attention1, true);
        writer.merge(2, 2, 0, 12, attention2, true);
        writer.merge(3, 3, 0, 12, attention3, true);
        writer.writeCellValue(0, 4, "*员工编号");
        writer.writeCellValue(1, 4, "*员工姓名");
        writer.writeCellValue(2, 4, "*角色名称");
        writer.writeCellValue(3, 4, "*部门名称");
        writer.writeCellValue(4, 4, "*岗位名称");
        writer.writeCellValue(5, 4, "*性别(男/女)");
        writer.writeCellValue(6, 4, "*民族");
        writer.writeCellValue(7, 4, "*籍贯");
        writer.writeCellValue(8, 4, "*政治面貌");
        writer.writeCellValue(9, 4, "*手机号(11位)");
        writer.writeCellValue(10, 4, "*证件号(18位)");
        writer.writeCellValue(11, 4, "*密码");
        writer.writeCellValue(12, 4, "现住址");

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

        // 设置提示语的样式
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                writer.setStyle(cellStyle, i, j);
            }
        }
        // 设置标题样式
        for (int i = 0; i < 13; i++) {
            writer.setStyle(cellStyle1, i, 4);
        }
        writer.setStyle(cellStyle1, 3, 4);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息批量导入模板", "UTF-8");
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
        List<Employee> employees = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Employee employee = new Employee();
            try {
                String empno = row.get(0).toString();
                if (!(empno.length() >= 3 && empno.length() <= 18)) {
                    throw new ServiceException();
                }
                employee.setEmpno(empno);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，员工编号长度在3到18个字符。");
            }
            try {
                String name = row.get(1).toString();
                if (!(name.length() >= 3 && name.length() <= 18)) {
                    throw new ServiceException();
                }
                employee.setName(name);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，员工姓名长度在3到20个字符。");
            }
            try {
                employee.setRoleId(roleService.getRoleIdByName(row.get(2).toString()));
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，角色不存在。");
            }
            try {
                employee.setDeptno(departmentService.getDeptnoByName(row.get(3).toString()));
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，部门不存在。");
            }
            // 判断对应的部门是否存在相应的岗位信息，若不存在抛出异常
            try {
                Post post = postService.getPostByName(row.get(4).toString());
                if (!(post.getDeptno().equals(employee.getDeptno()))) {
                    throw new ServiceException();
                }
                employee.setPostno(post.getPostno());
            } catch (Exception e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，岗位不存在。");
            }
            try {
                String sex = row.get(5).toString();
                if (!("男".equals(sex)) || "女".equals(sex)) {
                    throw new ServiceException();
                }
                employee.setSex(sex.equals("男") ? true : false);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，性别输入有误。");
            }
            try {
                String nation = row.get(6).toString();
                if (!(nation.length() >= 1 && nation.length() <= 10)) {
                    throw new ServiceException();
                }
                employee.setNation(nation);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，民族长度在1到10个字符。");
            }
            try {
                String province = row.get(7).toString();
                if (!(province.length() >= 2 && province.length() <= 60)) {
                    throw new ServiceException();
                }
                employee.setProvince(province);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，籍贯长度在1到10个字符。");
            }
            try {
                String political = row.get(8).toString();
                if (!(political.length() >= 2 && political.length() <= 20)) {
                    throw new ServiceException();
                }
                employee.setPolitical(political);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，政治面貌长度在1到10个字符。");
            }
            try {
                String phone = row.get(9).toString();
                String telReg = "1[356789]\\d{9}$";
                if (!phone.matches(telReg)) {
                    throw new ServiceException();
                }
                employee.setPhone(phone);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，手机号格式错误。");
            }
            try {
                String identifyNo = row.get(10).toString();
                if (identifyNo.length() != 18) {
                    throw new ServiceException();
                }
                employee.setIdentifyNo(identifyNo);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，证件号输入有误。");
            }
            try {
                String password = row.get(11).toString();
                if (!(password.length() >= 6 && password.length() <= 60)) {
                    throw new ServiceException();
                }
                employee.setPassword(password);
            } catch (ServiceException e) {
                throw new ServiceException(Constants.CODE_600, "导入失败，密码长度在6到60个字符。");
            }
            employee.setState(false);
            // 地址为非必填项
            try {
                employee.setAddress(row.get(12).toString());
            } catch (Exception ignored) {
                employee.setAddress("");
            }
            employees.add(employee);
        }
        try {
            employeeService.saveBatch(employees);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(Constants.CODE_600, "导入失败，员工编号、员工姓名或证件号重复。");
        }
        return Result.success(true);
    }
}
