package com.staffing.controller.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author JngKang
 * @date 2022-03-27 13:59
 * @description salary工资表添加名称
 */
@Setter
@Getter
public class SalaryPageDto {
    @ApiModelProperty("工资编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发放日期")
    private String payDate;

    @ApiModelProperty("员工编号")
    private String empno;

    private String empName;
    private String roleId;
    private String roleName;
    private String deptno;
    private String deptName;
    private String postno;
    private String postName;

    @ApiModelProperty("基本工资")
    private Float base;

    @ApiModelProperty("绩效工资")
    private Float performance;

    @ApiModelProperty("奖金")
    private Float bonus;

    @ApiModelProperty("补助")
    private Float subsidy;

    @ApiModelProperty("保险")
    private Float insurance;

    @ApiModelProperty("罚款")
    private Float penalty;

    @ApiModelProperty("缺勤")
    private Float absenteeism;

    @ApiModelProperty("实发工资")
    private Float fsalary;

    @ApiModelProperty("录入时间")
    private LocalDateTime inputTime;

    @ApiModelProperty("备注")
    private String remark;
}
