package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author JngKang
 * @date 2022-03-26
 * @description
 */
@Getter
@Setter
@ApiModel(value = "Salary对象", description = "工资表")
@TableName("salary")
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工资编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发放日期")
    private String payDate;

    @ApiModelProperty("员工编号")
    private String empno;

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
