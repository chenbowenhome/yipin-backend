package com.yipin.basic.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

@Data
public class DailySentenceVO implements Serializable {
    /**
     * 句子时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("句子时间")
    private Date nowDate;
    /**
     * 句子
     */
    @ApiModelProperty("句子")
    private String content;
    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String imgUrl;
}
