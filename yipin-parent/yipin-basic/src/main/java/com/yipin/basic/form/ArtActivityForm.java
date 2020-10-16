package com.yipin.basic.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ArtActivityForm implements Serializable {
    /**
     * 活动内容
     */
    @NotEmpty(message = "活动内容不能为空")
    private String content;
    /**
     * 活动图片
     */
    @NotEmpty(message = "活动图片不能为空")
    private String topicImage;
    /**
     * 活动标题
     */
    @NotEmpty(message = "活动标题不能为空")
    private String title;
    /**
     * 开始时间
     */
    @NotEmpty(message = "开始时间不能为空")
    private Date startTime;
    /**
     * 结束时间
     */
    @NotEmpty(message = "结束时间不能为空")
    private Date endTime;
    /**
     * 活动费用
     */
    @NotNull(message = "活动费用不能为空")
    private BigDecimal cost;

}
