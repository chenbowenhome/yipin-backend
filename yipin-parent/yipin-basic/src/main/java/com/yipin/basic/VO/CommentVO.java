package com.yipin.basic.VO;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CommentVO implements Serializable {
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * ID
     */
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
     * 话题文章id
     */
    private Integer topicArticleId;
    /**
     * 主评论id
     */
    private Integer mainCommentId;
    /**
     * 父评论id
     */
    private Integer parentCommentId;
    /**
     * 父评论用户名字
     */
    private String parentCommentNickname;
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
     * 回复该评论的列表
     */
    private List<CommentVO> replyCommentList;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 子评论个数
     */
    private Integer kidCommentNums;
}
