package com.staffing.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Constants;
import com.staffing.common.Result;
import com.staffing.controller.dto.UserDto;
import com.staffing.controller.dto.UserPasswordDTO;
import com.staffing.entity.User;
import com.staffing.exception.ServiceException;
import com.staffing.service.IUserService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
 * @date 2022-02-28 17:08
 * @description user
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * @param user 需要修改或添加的用户信息
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 新增和修改数据
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        try {
            return Result.success(userService.saveOrUpdate(user));
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "用户名重复，请更换用户名。");
        }
    }

    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO up) {
        return Result.success(userService.updatePassword(up));
    }

    /**
     * @param id 需要删除数据的id
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    /**
     * @param ids 存放批量删除数据的id的数组
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 批量删除数据
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    /**
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 查询所有数据
     */
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    /**
     * @param id 需要查询的id
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 根据id查询一条数据
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    /**
     * @param username 需要查询的username
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 根据username查询一条数据
     */
    @GetMapping("/username/{username}")
    public Result findOneByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    /**
     * @param pageNum  一页的数量
     * @param pageSize 页码
     * @param search   搜索的内容
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 分页查询和搜索
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String search,
                           @RequestParam String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(role)) {
            queryWrapper.eq("role", role);
        }
        if (!"".equals(search)) {
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("username", search)
                            .or().like("nickname", search)
                            .or().like("email", search)
            );
        }
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * @param userDto 用户登录的信息
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 登录验证
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        if (StrUtil.isBlank(userDto.getUsername()) || StrUtil.isBlank(userDto.getPassword())) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.login(userDto));
    }

    /**
     * @param response response
     * @author JngKang
     * @description 导出数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<User> list = null;
        List<Integer> ids = new ArrayList<>();
        // 从数据库查询出所有的数据
        if (ids.size() == 0) {
            list = userService.list();
        } else {
            list = userService.listByIds(ids);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER); //水平中间对齐，垂直中间对齐

        //自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("nickname", "昵称/姓名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("role", "角色");
        writer.addHeaderAlias("state", "状态");
        writer.addHeaderAlias("avatarUrl", "头像路径");

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
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
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

        List<String> row = CollUtil.newArrayList("用户名", "昵称/姓名", "密码", "邮箱");
        List<List<String>> rows = CollUtil.newArrayList();
        rows.add(row);
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.setColumnWidth(0, 20);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);

        writer.write(rows, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息批量导入模板", "UTF-8");
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
    @PostMapping("/import")
    public Result imp(@RequestParam MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setId(0);
            user.setUsername(row.get(0).toString());
            user.setNickname(row.get(1).toString());
            user.setPassword(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setRole("学生");
            user.setState(false);
            System.out.println(user);
            users.add(user);
        }
        try {
            userService.saveBatch(users);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_600, "导入失败，数据中存在用户名重复或与已添加用户的用户名重复。");
        }
        return Result.success(true);
    }
}

