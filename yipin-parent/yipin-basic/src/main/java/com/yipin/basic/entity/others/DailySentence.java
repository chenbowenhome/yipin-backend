package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 句子状态
     */
    @ApiModelProperty("句子状态")
    private Integer nowStatus;
}
