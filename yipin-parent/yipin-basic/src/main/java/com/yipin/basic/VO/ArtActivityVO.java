package com.yipin.basic.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(value = "活动视图层")
@Data
public class ArtActivityVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 活动图片
     */
    @ApiModelProperty("活动图片")
    private String topicImage;
    /**
     * 活动标题
     */
    @ApiModelProperty("活动标题")
    private String title;
    /**
     * 活动内容
     */
    @ApiModelProperty("活动内容")
    private String content;
    /**
     * 评论数
     */
    /*@ApiModelProperty("评论数")
    private Integer comments;*/
    /**
     * 浏览次数
     */
   /* @ApiModelProperty("浏览次数")
    private Integer views;*/
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer likes;
    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("活动开始时间")
    private Date startTime;
    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("活动结束时间")
    private Date endTime;
    /**
     * 活动参加人数
     */
    @ApiModelProperty("活动参加人数")
    private Integer participants;
    /**
     * 活动费用
     */
    @ApiModelProperty("活动费用")
    private BigDecimal cost;
    /**
     * 是否结束，0为未结束，1为已结束，默认为0
     */
    @ApiModelProperty("是否结束，0为未结束，1为已结束，默认为0")
    private Integer isEnd;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String activityProductId;
    /**
     * 活动附属商品ids
     */
    @ApiModelProperty("活动附属商品ids")
    private List<String> othersProductIds; //TODO 这里需要改为商品集合
    /**
     * 判断用户是否参加该活动
     */
    @ApiModelProperty("判断用户是否参加该活动")
    private Integer isJoin;

}
