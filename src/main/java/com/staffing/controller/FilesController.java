package com.staffing.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.staffing.common.Result;
import com.staffing.entity.Files;
import com.staffing.mapper.FilesMapper;
import com.staffing.service.IFilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-04
 * @description 文件
 */
@RestController
@RequestMapping("/files")
public class FilesController {

    @Value("${files.upload.path}")
    private String filesUploadPath;

    @Resource
    private FilesMapper fileMapper;

    @Resource
    private IFilesService filesService;

    /**
     * @param files 更新的数据
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 更新数据
     */
    @PostMapping()
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    /**
     * @param id 需要删除数据的id
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 根据id删除数据
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(filesService.removeById(id));
    }

    /**
     * @param ids 需要删除数据的id数组
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 虚假批量删除数据，将需要删除数据中删除字段修改为true
     */
    @PostMapping("/del/batch")
    public Result deleteBatchFake(@RequestBody List<Integer> ids) {
        return Result.success(filesService.removeByIdsFake(ids));
    }

    /**
     * @return java.util.List<com.staffing.entity.Files>
     * @author JngKang
     * @description 查询所有数据
     */
    @GetMapping
    public List<Files> findAll() {
        return filesService.list();
    }

    /**
     * @param pageNum  一页的数据量
     * @param pageSize 页码
     * @param search   搜索的内容
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.staffing.entity.Files>
     * @author JngKang
     * @description 分页查询和搜索
     */
    @GetMapping("/page")
    public Page<Files> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam String search) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        if (!"".equals(search)) {
            // 检索条件
            queryWrapper.like("name", search);
        }
        return filesService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * @param file 前端传递过来的文件
     * @return com.staffing.common.Result
     * @author JngKang
     * @description 文件上传接口
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置文件的父级目录是否存在，托不存在则创建一个新的文件目录
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

        String url;
        // 上传文件到磁盘
        file.transferTo(uploadFile);
        // 获取文件的md5
        String md5 = SecureUtil.md5(uploadFile);
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            // 由于文件已存在，所以删除刚才上传的重复文件
            uploadFile.delete();
        } else {
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/files/" + fileUUID;
        }

        // 存储到数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        // 转换单位为KB
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return Result.success(saveFile);
    }

    /**
     * @param fileUUID 文件的名称
     * @param response 请求
     * @author JngKang
     * @description 文件的下载
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识标识码获取文件
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 设置输入流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
}

