package com.yipin.basic.VO;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class TopicArticleVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 文章图片
     */
    private String topicImage;
    /**
     * 文章标题
     */
    private String title;
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

    private Date createTime;

    private Date updateTime;
}
