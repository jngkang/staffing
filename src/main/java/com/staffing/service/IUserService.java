package com.staffing.service;

import com.staffing.controller.dto.UserDto;
import com.staffing.controller.dto.UserPasswordDTO;
import com.staffing.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<User> {

    // 登录验证
    UserDto login(UserDto userDto);

    Boolean updatePassword(UserPasswordDTO up);
}
