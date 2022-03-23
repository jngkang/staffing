package com.staffing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.staffing.common.Constants;
import com.staffing.entity.Post;
import com.staffing.exception.ServiceException;
import com.staffing.mapper.PostMapper;
import com.staffing.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author JngKang
 * @date 2022-03-22
 * @description
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Override
    public boolean insert(Post post) {
        Post id = getById(post.getPostno());
        if (id != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，岗位编号重复。");
        }
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", post.getName());
        queryWrapper.eq("deptno", post.getDeptno());
        Post name = getOne(queryWrapper);
        if (name != null) {
            throw new ServiceException(Constants.CODE_600, "添加失败，岗位名称重复。");
        }
        return save(post);
    }
}
