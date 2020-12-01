package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.VO.DailySentenceVO;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.DailySentence;
import com.yipin.basic.form.ArtActivityForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtActivityService {
    /**通过id获取活动详情**/
    Result<ArtActivityVO> getArticleById(Integer id,Integer userId);
    /**上传活动**/
    Result<Void> addTopicArticle(ArtActivityForm artActivityForm);
    /**报名参加活动**/
    Result<Void> applyActivity(Integer userId,Integer artActivityId);
    /**为活动点赞**/
    Result<Void> likeActivity(Integer artActivityId);
    /**为活动取消点赞**/
    Result<Void> unlikeActivity(Integer artActivityId);
    /**查找全部每日一句**/
    Result<PageVO<DailySentenceVO>> listAllDailySentence(PageArg arg);
    /**分页获取最新活动**/
    Result<PageVO<ArtActivityVO>> listTopicArticle(Integer userId, PageArg arg);

    /** all 查询所有的活动**/
    Result<PageVO<ArtActivityVO>> findAllActivity(String keyWord,Integer userId,PageArg arg);
    /** banner 获取活动轮播图**/
    Result<PageVO<ArtActivityVO>> listTopicArticleSlide(String keyWord,Integer userId,Integer slideStatus,PageArg arg);
    /** end 查询已经结束的活动**/
    Result<PageVO<ArtActivityVO>> findEndActivity(String keyWord,Integer userId,Integer isEnd,PageArg arg);
    /** product 查询产品类的活动**/
    Result<PageVO<ArtActivityVO>> findProductActivity(String keyWord,Integer userId,Integer productType,PageArg arg);
    /** online 查询线上0， offline 线下1 类活动**/
    Result<PageVO<ArtActivityVO>> findOnlineActivity(String keyWord,Integer userId,Integer onlineStatus,PageArg arg);
    /** join 查询用户已经参加的活动**/
    Result<PageVO<ArtActivityVO>> findUserJoinedActivity(String keyWord,Integer userId,PageArg arg);
    /** collect 查询用户已经付款的活动**/
    Result<PageVO<ArtActivityVO>> findUserCollectedActivity(String keyWord,Integer userId,PageArg arg);
}
