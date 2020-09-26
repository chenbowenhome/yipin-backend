package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 作品id
     */
    private Integer productionId;
    /**
     * 父评论id
     */
    private Integer parentCommentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否被删除，0为未删除，1为已删除
     */
    private Boolean deleteStatus;
    /**
     * 主评论id
     */
    private Integer mainCommentId;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 子评论个数
     */
    private Integer kidCommentNums;
    /**
     * 话题文章id
     */
    private Integer topicArticleId;

}
