package com.staffing.service;

import com.staffing.entity.Files;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author JngKang
 * @date 2022-03-04
 * @description
 */
public interface IFilesService extends IService<Files> {

    /**
     * @param ids 需要删除的数据
     * @return java.lang.Object
     * @author JngKang
     * @description 虚假批量删除，将需要删除的数据字段中的设置为已删除
     */
    Integer removeByIdsFake(List<Integer> ids);
}
