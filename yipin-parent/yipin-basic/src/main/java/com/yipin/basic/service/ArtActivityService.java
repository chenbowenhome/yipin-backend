package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.form.ArtActivityForm;

public interface ArtActivityService {
    /**通过id获取活动详情**/
    Result<ArtActivityVO> getArticleById(Integer id,Integer userId);
    /**上传活动**/
    Result<Void> addTopicArticle(ArtActivityForm artActivityForm);
    /**分页获取最新活动**/
    Result<PageVO<ArtActivityVO>> listTopicArticle(Integer userId, PageArg arg);
    /**报名参加活动**/
    Result<Void> applyActivity(Integer userId,Integer artActivityId);
    /**为活动点赞**/
    Result<Void> likeActivity(Integer artActivityId);
    /**为活动取消点赞**/
    Result<Void> unlikeActivity(Integer artActivityId);
}
