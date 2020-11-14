package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "评论表")
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
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 作品id
     */
    @ApiModelProperty("作品id")
    private Integer productionId;
    /**
     * 父评论id
     */
    @ApiModelProperty("父评论id")
    private Integer parentCommentId;
    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
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
    @ApiModelProperty("是否被删除，0为未删除，1为已删除")
    private Boolean deleteStatus;
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer likes;
    /**
     * 话题文章id
     */
    @ApiModelProperty("活动id")
    private Integer activityId;

}
