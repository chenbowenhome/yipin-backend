package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.dao.othersDao.DailySentenceRepository;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.DailySentence;
import com.yipin.basic.service.ArtActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "艺期活动相关接口")
@RestController
@RequestMapping("/topicArticle")
public class ArtActivityController {

    @Autowired
    private ArtActivityService artActivityService;
    @Autowired
    private DailySentenceRepository dailySentenceRepository;

    /**
     * 通过id获取话题文章
     **/
    @ApiOperation("通过id获取活动")
    @RequestMapping(value = "/getArticleById", method = RequestMethod.GET)
    public Result<ArtActivityVO> getArticleById(Integer id,Integer userId) {
        return artActivityService.getArticleById(id,userId);
    }

    /**
     * 分页获取最新话题
     **/
    @ApiOperation("分页获取最新活动")
    @RequestMapping(value = "/listTopicArticle", method = RequestMethod.POST)
    public Result<PageVO<ArtActivityVO>> listTopicArticle(Integer userId, @RequestBody PageArg arg) {
        arg.validate();
        return artActivityService.listTopicArticle(userId, arg);
    }

    /**
     * 获取每日一句
     **/
    @ApiOperation("获取每日一句")
    @RequestMapping(value = "/getDailySentence", method = RequestMethod.GET)
    public Result<DailySentence> getDailySentence() {
        List<DailySentence> dailySentenceList = dailySentenceRepository.findDailySentenceByNowStatus(1);
        if (dailySentenceList.size() == 0) {
            return Result.newError("系统错误");
        }
        return Result.newSuccess(dailySentenceList.get(0));
    }

    /**
     * 报名参加活动
     **/
    @ApiOperation("报名参加活动")
    @RequestMapping(value = "/applyActivity", method = RequestMethod.POST)
    public Result<Void> applyActivity(Integer userId, Integer artActivityId) {
        return artActivityService.applyActivity(userId, artActivityId);
    }

    /**
     * 为活动点赞
     **/
    @ApiOperation("为活动点赞")
    @RequestMapping(value = "/likeActivity", method = RequestMethod.POST)
    public Result<Void> likeActivity(Integer artActivityId) {
        return artActivityService.likeActivity(artActivityId);
    }

    /**
     * 为活动取消点赞
     **/
    @ApiOperation("为活动取消点赞")
    @RequestMapping(value = "/unlikeActivity", method = RequestMethod.POST)
    Result<Void> unlikeActivity(Integer artActivityId) {
        return artActivityService.unlikeActivity(artActivityId);
    }
}
