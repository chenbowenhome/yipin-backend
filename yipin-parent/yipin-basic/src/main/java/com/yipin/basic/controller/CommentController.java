package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.Comment;
import com.yipin.basic.form.CommentForm;
import com.yipin.basic.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "评论相关接口")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**评论作品**/
    @ApiOperation("评论作品")
    @RequestMapping(value = "/commentProduction",method = RequestMethod.POST)
    public Result<Void> commentProduction(@Valid @RequestBody CommentForm commentForm){
        return commentService.commentProduction(commentForm);
    }
    /**分页获取作品评论信息**/
    @ApiOperation("分页获取作品评论信息")
    @RequestMapping(value = "/listComments",method = RequestMethod.POST)
    public Result<PageVO<Comment>> listComments(Integer productionId,@RequestBody PageArg arg){
        arg.validate();
        return commentService.listComments(productionId,arg);
    }
    /**删除一条评论**/
    @ApiOperation("删除一条评论")
    @RequestMapping(value = "/deleteComment",method = RequestMethod.POST)
    public Result<Void> deleteComment(Integer id){
        return commentService.deleteComment(id);
    }
    /**更新一条评论**/
    @ApiOperation("更新一条评论")
    @RequestMapping(value = "/updateComment",method = RequestMethod.POST)
    public Result<Void> updateComment(Integer id,@Valid @RequestBody CommentForm commentForm){
        return commentService.updateComment(id,commentForm);
    }
}
