package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.CommentVO;
import com.yipin.basic.entity.others.Comment;
import com.yipin.basic.entity.others.Likes;
import com.yipin.basic.form.CommentForm;
import com.yipin.basic.service.CommentService;
import enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@Api(tags = "评论相关接口(作品详情页面 与 活动详情页面)")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 评论作品
     **/
    @ApiOperation("评论作品传作品id:productionId，评论活动传活动id:activityId;parentCommentId:回复评论的id;content:评论内容")
    @RequestMapping(value = "/commentProduction", method = RequestMethod.POST)
    public Result<Void> commentProduction(@Valid @RequestBody CommentForm commentForm) {
        return commentService.commentProduction(commentForm);
    }

    /**
     * 分页获取作品评论信息
     **/
    @ApiOperation("分页获取画作下评论信息(传入作品id和分页参数，返回作品详情)")
    @RequestMapping(value = "/listComments", method = RequestMethod.POST)
    public Result<PageVO<CommentVO>> listComments(Integer productionId, @RequestBody PageArg arg) {
        arg.validate();
        return commentService.listComments(productionId, arg);
    }

    /**分页获取话题文章评论信息**/
    @ApiOperation("分页获取活动评论信息(传入活动id与分页参数，返回活动详情)")
    @RequestMapping(value = "/listTopicArticleComments", method = RequestMethod.POST)
    public Result<PageVO<CommentVO>> listTopicArticleComments(Integer topicArticleId,@RequestBody PageArg arg) {
        arg.validate();
        return commentService.listTopicArticleComments(topicArticleId,arg);
    }

    /**
     * 删除一条评论
     **/
    @ApiOperation("删除一条评论(传入活动id)")
    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    public Result<Void> deleteComment(Integer id) {
        return commentService.deleteComment(id);
    }

    /**
     * 更新一条评论
     **/
    @ApiOperation("更新一条评论(传入活动id)")
    @RequestMapping(value = "/updateComment", method = RequestMethod.POST)
    public Result<Void> updateComment(Integer id, @Valid @RequestBody CommentForm commentForm) {
        return commentService.updateComment(id, commentForm);
    }

    /**
     * 为评论点赞
     **/
    @ApiOperation("为评论点赞(传入活动id)")
    @RequestMapping(value = "/likeComment", method = RequestMethod.POST)
    public Result<Void> likeComment(Integer userId, Integer commentId) {
        return commentService.likeComment(userId, commentId);
    }

    /**
     * 取消点赞
     **/
    @ApiOperation("取消点赞(传入用户id和活动id)")
    @RequestMapping(value = "/unlikeComment", method = RequestMethod.POST)
    public Result<Void> unlikeComment(Integer userId, Integer commentId) {
        return commentService.unlikeComment(userId, commentId);
    }
}
