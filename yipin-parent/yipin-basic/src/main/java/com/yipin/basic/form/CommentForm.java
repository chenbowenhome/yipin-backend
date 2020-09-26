package com.yipin.basic.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CommentForm implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id",required = true)
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    /**
     * 作品id
     */
    @ApiModelProperty(value = "作品id")
    private Integer productionId;
    /**
     * 话题文章id
     */
    @ApiModelProperty(value = "话题文章id")
    private Integer topicArticleId;
    /**
     * 主评论id
     */
    @ApiModelProperty(value = "主评论id")
    private Integer mainCommentId;
    /**
     * 父评论id
     */
    @ApiModelProperty(value = "父评论id")
    private Integer parentCommentId;
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容",required = true)
    @NotEmpty(message = "评论内容不能为空")
    private String content;
}
