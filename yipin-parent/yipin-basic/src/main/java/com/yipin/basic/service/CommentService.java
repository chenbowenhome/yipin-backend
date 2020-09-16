package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.others.Comment;
import com.yipin.basic.form.CommentForm;

public interface CommentService {
    /**评论作品**/
    Result<Void> commentProduction(CommentForm commentForm);
    /**分页获取作品评论信息**/
    Result<PageVO<Comment>> listComments(Integer productionId,PageArg arg);
    /**删除一条评论**/
    Result<Void> deleteComment(Integer id);
    /**更新一条评论**/
    Result<Void> updateComment(Integer id,CommentForm commentForm);
}
