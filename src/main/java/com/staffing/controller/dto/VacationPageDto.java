package com.staffing.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author JngKang
 * @date 2022-03-29 20:39
 * @description vacation分页查询实体类
 */
@Getter
@Setter
public class VacationPageDto {
    @ApiModelProperty("总请假编号")
    private Integer id;

    @ApiModelProperty("员工编号")
    private String empno;

    private String empName;
    private String roleId;
    private String roleName;
    private String deptno;
    private String deptName;
    private String postno;
    private String postName;

    @ApiModelProperty("请假原因")
    private String reason;

    @ApiModelProperty("假期开始时间")
    private LocalDateTime startDatetime;

    @ApiModelProperty("假期结束时间")
    private LocalDateTime endDatetime;

    @ApiModelProperty("申请日期")
    private LocalDateTime inputDatetime;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("员工编号")
    private String checkEmpno;
    private String checkEmpName;

    @ApiModelProperty("审核日期")
    private LocalDateTime checkDatetime;
}
