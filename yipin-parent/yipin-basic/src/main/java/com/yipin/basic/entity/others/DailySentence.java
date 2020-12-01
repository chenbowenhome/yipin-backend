package com.yipin.basic.entity.others;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "每日一句表")
@Data
@Entity
@Table(name = "daily_sentence")
public class DailySentence implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 句子状态
     */
    @ApiModelProperty("句子状态")
    private Integer nowStatus;
}
