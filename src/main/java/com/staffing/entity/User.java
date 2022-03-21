package com.staffing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author JngKang
 * @date 2022-03-03 18:55
 * @description 文件实体类
 */
@Getter
@Setter
@TableName("user")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id = 0;

    private String username;

    private String nickname;

    private String password;

    private String email;

    private String role;

    private Boolean state;

    private String avatarUrl;

}
