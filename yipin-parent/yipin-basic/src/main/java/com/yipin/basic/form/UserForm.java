package com.yipin.basic.form;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserForm implements Serializable {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * E-Mail
     */
    private String email;
    /**
     * 用户描述
     */
    private String description;
    /**
     * 用户手机号
     */
    private String mobile;
    /**
     * 所在单位
     */
    private String unit;


}
