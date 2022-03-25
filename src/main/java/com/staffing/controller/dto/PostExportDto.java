package com.staffing.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author JngKang
 * @date 2022-03-23 15:28
 * @description 导出excel时使用此实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostExportDto {
    /**
     * 岗位编号
     */
    private String postno;
    /**
     * 部门名称
     */
    private String dname;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
}
