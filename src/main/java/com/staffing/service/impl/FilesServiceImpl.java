package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.staffing.entity.Files;
import com.staffing.mapper.FilesMapper;
import com.staffing.service.IFilesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-04
 * @description
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {

    @Resource
    FilesMapper filesMapper;

    @Override
    public Integer removeByIdsFake(List<Integer> ids) {
        Integer rows = 0;
        UpdateWrapper<Files> updateWrapper = null;
        for (Integer id : ids) {
            updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("files_id", id);
            Files files = new Files();
            files.setIsDelete(true);
            rows += filesMapper.update(files, updateWrapper);
        }
        return rows;
    }
}
