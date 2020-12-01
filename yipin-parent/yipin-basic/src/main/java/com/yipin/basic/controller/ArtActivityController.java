package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.VO.DailySentenceVO;
import com.yipin.basic.dao.othersDao.DailySentenceNowRepository;
import com.yipin.basic.dao.othersDao.DailySentenceRepository;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.DailySentence;
import com.yipin.basic.entity.others.DailySentenceNow;
import com.yipin.basic.service.ArtActivityService;
import enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "艺期活动相关接口(发现页面)")
@RestController
@RequestMapping("/topicArticle")
public class ArtActivityController {

    @Autowired
    private ArtActivityService artActivityService;
    @Autowired
    private DailySentenceRepository dailySentenceRepository;
    @Autowired
    private DailySentenceNowRepository dailySentenceNowRepository;

    /**
     * 通过id获取话题文章
     **/
    @ApiOperation("通过id获取活动(传入活动的id，返回活动的详细信息)")
    @RequestMapping(value = "/getArticleById", method = RequestMethod.GET)
    public Result<ArtActivityVO> getArticleById(Integer id,@RequestParam(required = false) Integer userId) {
        return artActivityService.getArticleById(id,userId);
    }

    /**
     * 分页获取最新话题
     **/
    @ApiOperation("分页获取最新活动(如果用户登录了就传用户的id，没有登录可以不传，分页参数pageNo为当前页数，pageSize为每页大小，返回的是活动轮播图下方的活动)")
    @RequestMapping(value = "/listTopicArticle", method = RequestMethod.POST)
    public Result<PageVO<ArtActivityVO>> listTopicArticle(@RequestParam(required = false) Integer userId, @RequestBody PageArg arg) {
        arg.validate();
        return artActivityService.listTopicArticle(userId, arg);
    }

    /**
     * 获取活动轮播图
     *
    @ApiOperation("获取活动轮播图(如果用户登录了就传用户的id，没有登录可以不传，返回的是作为轮播图的活动信息)")
    @RequestMapping(value = "/listTopicArticleSlide", method = RequestMethod.POST)
    public Result<List<ArtActivityVO>> listTopicArticleSlide(@RequestParam(required = false) Integer userId) {
        return artActivityService.listTopicArticleSlide(userId);
    }*/

    /**
     * 获取每日一句
     **/
    @ApiOperation("获取每日一句")
    @RequestMapping(value = "/getDailySentence", method = RequestMethod.GET)
    public Result<DailySentenceVO> getDailySentence() {
        DailySentenceNow dailySentenceNow = dailySentenceNowRepository.findTop1();
        if (dailySentenceNow == null) {
            return Result.newError("系统错误");
        }
        DailySentence dailySentence = dailySentenceRepository.findDailySentenceById(dailySentenceNow.getDailySentenceId());
        if (dailySentence == null){
            return Result.newError("系统错误");
        }
        DailySentenceVO dailySentenceVO = new DailySentenceVO();
        dailySentenceVO.setContent(dailySentence.getContent());
        dailySentenceVO.setImgUrl(dailySentence.getImgUrl());
        dailySentenceVO.setNowDate(dailySentenceNow.getNowDate());
        return Result.newSuccess(dailySentenceVO);
    }

    /**
     * 获取20句
     **/
    @ApiOperation("获取20句子")
    @RequestMapping(value = "/get20Sentence", method = RequestMethod.GET)
    public Result<List<DailySentenceVO>> get20Sentence() {
        List<DailySentenceNow> dailySentenceList = dailySentenceNowRepository.findTop20();
        List<DailySentenceVO> dailySentenceVOList = new ArrayList<>();
        for (DailySentenceNow dailySentenceNow : dailySentenceList) {
            DailySentence dailySentence = dailySentenceRepository.findDailySentenceById(dailySentenceNow.getDailySentenceId());
            if (dailySentence != null) {
                DailySentenceVO dailySentenceVO = new DailySentenceVO();
                dailySentenceVO.setNowDate(dailySentenceNow.getNowDate());
                dailySentenceVO.setImgUrl(dailySentence.getImgUrl());
                dailySentenceVO.setContent(dailySentence.getContent());
                dailySentenceVOList.add(dailySentenceVO);
            }
        }
        return Result.newSuccess(dailySentenceVOList);
    }

    /**
     * 报名参加活动
     **/
    @ApiOperation("报名参加活动(需要传入用户id与参加的活动的id，返回报名是否成功信息)")
    @RequestMapping(value = "/applyActivity", method = RequestMethod.POST)
    public Result<Void> applyActivity(Integer userId, Integer artActivityId) {
        return artActivityService.applyActivity(userId, artActivityId);
    }

    /**
     * 为活动点赞
     **/
    @ApiOperation("为活动点赞(传入活动的id)")
    @RequestMapping(value = "/likeActivity", method = RequestMethod.POST)
    public Result<Void> likeActivity(Integer artActivityId) {
        return artActivityService.likeActivity(artActivityId);
    }

    /**
     * 为活动取消点赞
     **/
    @ApiOperation("为活动取消点赞(传入活动的id)")
    @RequestMapping(value = "/unlikeActivity", method = RequestMethod.POST)
    Result<Void> unlikeActivity(Integer artActivityId) {
        return artActivityService.unlikeActivity(artActivityId);
    }


    @ApiOperation("获取全部每日一句(分页获取数据库中所有的每日一句)")
    @RequestMapping(value = "/listAllDailySentence", method = RequestMethod.POST)
    public Result<PageVO<DailySentenceVO>> listAllDailySentence(@RequestBody PageArg arg) {
        arg.validate();
        return artActivityService.listAllDailySentence(arg);
    }

    @ApiOperation("api-V1A001 分页获取所有活动（按时间降序排列）")
    @RequestMapping(value = "/getByList", method = RequestMethod.POST)
    public Result<PageVO<ArtActivityVO>> getByList(Integer userId,@RequestBody PageArg arg,String keyWord,String actType) {
        arg.validate();
        if (actType == null || actType.equals("")){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }else if(actType.equals("all")){
            return artActivityService.findAllActivity(keyWord,userId,arg);
        }else if(actType.equals("end")){
            return artActivityService.findEndActivity(keyWord,userId,1,arg);
        }else if(actType.equals("banner")){
            return artActivityService.listTopicArticleSlide(keyWord,userId,1,arg);
        }else if(actType.equals("product")){
            return artActivityService.findProductActivity(keyWord,userId,1,arg); //是否为产品类活动
        }else if(actType.equals("online")){
            return artActivityService.findOnlineActivity(keyWord,userId,0,arg); //是否为线上活动
        }else if(actType.equals("offline")){
            return artActivityService.findOnlineActivity(keyWord,userId,1,arg); //是否为线下活动
        }else if(actType.equals("join")){
            return artActivityService.findUserJoinedActivity(keyWord,userId,arg);
        }else if(actType.equals("collect")){
            return artActivityService.findUserCollectedActivity(keyWord,userId,arg);
        }
        return Result.newError("输入参数有误");
    }
}
