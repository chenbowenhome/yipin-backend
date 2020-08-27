package com.yipin.basic.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "轮播图表单")
public class SlideForm implements Serializable {
    /**
     * 作品url
     */
    @ApiModelProperty(value = "轮播图图片url",required = true)
    @NotEmpty(message = "轮播图图片url不能为空")
    private String url;
    /**
     * 排序，越大的越在前面
     */
    @ApiModelProperty(value = "轮播图次序，越大排得越靠前",required = true,example = "999")
    @NotNull(message = "轮播图次序不能为空")
    private Integer orderNum;
    /**
     * 备注
     */
    @ApiModelProperty(value = "轮播图备注",required = true)
    @NotEmpty(message = "轮播图备注不能为空")
    private String remark;
}
