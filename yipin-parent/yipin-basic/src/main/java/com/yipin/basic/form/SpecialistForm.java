package com.yipin.basic.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "专家信息表单")
public class SpecialistForm implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id",required = true,example = "10")
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    /**
     * 名字
     */
    @ApiModelProperty(value = "专家姓名",required = true,example = "张三")
    @NotEmpty(message = "专家姓名不能为空")
    private String name;
    /**
     * 专业
     */
    @ApiModelProperty(value = "专家专业",required = true,example = "艺术设计专业")
    @NotEmpty(message = "专业不能为空")
    private String major;
    /**
     * 毕业院校
     */
    @ApiModelProperty(value = "毕业院校",required = true,example = "武汉大学")
    @NotEmpty(message = "毕业院校不能为空")
    private String university;
    /**
     * 教龄
     */
    @ApiModelProperty(value = "教龄",required = true,example = "3")
    @NotNull(message = "教龄不能为空")
    private Integer teachingAge;
    /**
     * 擅长年龄端
     */
    @ApiModelProperty(value = "擅长年龄段",required = true,example = "19~27岁之间")
    @NotEmpty(message = "擅长年龄段不能为空")
    private String adeptAge;
    /**
     * 擅长画种
     */
    @ApiModelProperty(value = "擅长画种",required = true)
    @NotEmpty(message = "擅长画种不能为空")
    private List<Integer> adeptPaint;
    /**
     * 所属机构
     */
    @ApiModelProperty(value = "所属机构",required = true,example = "xx公司")
    @NotEmpty(message = "所属机构不能为空")
    private String organization;
    /**
     * 学生数量
     */
    @ApiModelProperty(value = "学生数",required = true,example = "55")
    @NotNull(message = "学生数不能为空")
    private Integer studentNums;
    /**
     * 教师资格证书
     */
    @ApiModelProperty(value = "资格证书",required = true,example = "www.xxxxxxx.jpg")
    @NotEmpty(message = "资格证书不能为空")
    private String teacherCertification;
    /**
     * 获奖证书
     */
    @ApiModelProperty(value = "获奖证书")
    private List<String> prizeCertification;

}
