package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "topic_article")
public class TopicArticle implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 文章内容
     */
    private String content;
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
