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
    boolean insert(Post post);

    /**
     * @param name 岗位名称
     * @return java.lang.String
     * @description 根据岗位名称查询岗位编号
     */
    Post getPostByName(String name);

}
