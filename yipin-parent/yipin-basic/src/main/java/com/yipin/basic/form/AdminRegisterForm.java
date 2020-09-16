package com.yipin.basic.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AdminRegisterForm implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "用户密码",required = true)
    @NotEmpty(message = "用户密码不能为空")
    private String password;
    /**
     * 名字
     */
    @ApiModelProperty(value = "用户名字",required = true)
    @NotEmpty(message = "用户名字不能为空")
    private String name;

    @ApiModelProperty(value = "管理员电话",required = true)
    @NotEmpty(message = "管理员电话不能为空")
    private String mobile;

    @ApiModelProperty(value = "管理员性别",required = true)
    @NotNull(message = "管理员性别不能为空")
    private Integer sex;
}
