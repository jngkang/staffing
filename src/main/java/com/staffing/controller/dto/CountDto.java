package com.staffing.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JngKang
 * @date 2022-03-26 14:53
 * @description 只有部门（岗位）名称和部门（岗位）人员数量两个字段
 */
@Getter
@Setter
public class CountDto {
    /**
     * 名称
     */
    String name;
    /**
     * 总人数
     */
    Integer value;
}
