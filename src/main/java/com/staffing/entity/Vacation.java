package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author JngKang
 * @date 2022-03-29
 * @description
 */
@Getter
@Setter
@ApiModel(value = "Vacation对象", description = "请假表")
@TableName("vacation")
@ToString
public class Vacation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总请假编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工编号")
    private String empno;

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

    @ApiModelProperty("审核日期")
    private LocalDateTime checkDatetime;


}
