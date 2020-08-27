package com.yipin.basic.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserMsgForm implements Serializable {
    /**
     * 个人简介
     */
    private String description;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    private Integer sex;
    /**
     * 所属单位
     */
    @NotEmpty(message = "所属单位不能为空")
    private String unit;

    /**
     * 用户身份
     */
    @NotNull(message = "用户身份不能为空")
    private Integer identity;
}
