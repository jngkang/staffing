package com.staffing.service.impl;

import com.staffing.entity.Post;
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

}
