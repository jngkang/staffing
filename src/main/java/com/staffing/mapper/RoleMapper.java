package com.staffing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.staffing.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author JngKang
 * @date 2022-03-08
 * @description
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select role_id from sys_role where name = #{name}")
    Integer selectByName(@Param("name") String name);
}
