package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-04
 * @description
 */
@Getter
@Setter
@TableName("sys_files")
@ApiModel(value = "Files对象", description = "")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "files_id", type = IdType.AUTO)
    private Integer filesId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小，单位KB")
    private Long size;

    @ApiModelProperty("下载链接")
    private String url;

    @ApiModelProperty("文件的唯一标识md5")
    private String md5;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

}
