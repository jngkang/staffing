package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-11
 * @description
 */
@Getter
@Setter
@TableName("sys_role_menu")
@ApiModel(value = "RoleMenu对象", description = "")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    // roleId&menuId为联合主键，无法添加@TableId注解

    private Integer roleId;

    private Integer menuId;


}
