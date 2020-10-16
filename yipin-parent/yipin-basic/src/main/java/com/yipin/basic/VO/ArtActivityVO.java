package com.yipin.basic.VO;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ArtActivityVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 活动图片
     */
    private String topicImage;
    /**
     * 活动标题
     */
    private String title;
    /**
     * 活动内容
     */
    private String content;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 浏览次数
     */
    private Integer views;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 活动参加人数
     */
    private Integer participants;
    /**
     * 活动费用
     */
    private BigDecimal cost;
    /**
     * 是否结束，0为未结束，1为已结束，默认为0
     */
    private Integer isEnd;

    private Date createTime;
    /**
     * 判断用户是否参加该活动
     */
    private Integer isJoin;

}
