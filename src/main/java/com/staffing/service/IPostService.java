package com.staffing.service;

import com.staffing.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
public interface IPostService extends IService<Post> {

    /**
     * @param post 新增的数据
     * @return boolean
     * @description 添加数据
     */
    public boolean insert(Post post);

}
