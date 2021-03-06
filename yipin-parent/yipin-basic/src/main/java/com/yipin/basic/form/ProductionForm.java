package com.yipin.basic.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "上传作品表单")
public class ProductionForm implements Serializable {

    /**
     * 作品标题
     */
    @ApiModelProperty(value = "作品标题",required = true,example = "动漫之眼")
    @NotEmpty(message = "作品标题不能为空")
    private String title;
    /**
     * 作品描述
     */
    @ApiModelProperty(value = "作品描述",example = "动漫人物眼睛手绘")
    private String description;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户的id",required = true,example = "2")
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    /**
     * 发布状态
     */
    @ApiModelProperty(value = "公开状态，0为不公开，1为公开",required = true,example = "0")
    @NotNull(message = "公开状态不能为空")
    private Integer publishStatus;
    /**
     * 评估状态
     */
    @ApiModelProperty(value = "评估状态，0为未评估，1为已评估",required = true,example = "0")
    @NotNull(message = "评估状态不能为空")
    private Integer evaluateStatus;
    /**
     * 是否设置为代表作
     */
    @ApiModelProperty(value = "是否为代表作，0为不是，1为是",required = true,example = "0")
    @NotNull(message = "是否为代表作不能为空")
    private Integer isMainProduction;
    /**
     * 分类标签id
     */
    @ApiModelProperty(value = "分类标签id",required = true)
    @NotNull(message = "分类标签id不能为空")
    private Integer tagId;
    /**
     * 图片url
     */
    @ApiModelProperty(value = "图片url",required = true)
    @NotEmpty(message = "图片url不能为空")
    private List<String> imgUrl;

}
