package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.TopicArticleVO;
import com.yipin.basic.dao.othersDao.DailySentenceRepository;
import com.yipin.basic.entity.others.DailySentence;
import com.yipin.basic.entity.others.TopicArticle;
import com.yipin.basic.service.TopicArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "话题文章相关接口")
@RestController
@RequestMapping("/topicArticle")
public class TopicArticleController {

    @Autowired
    private TopicArticleService topicArticleService;
    @Autowired
    private DailySentenceRepository dailySentenceRepository;

    /**通过id获取话题文章**/
    @ApiOperation("通过id获取话题文章")
    @RequestMapping(value = "/getArticleById",method = RequestMethod.GET)
    public Result<TopicArticle> getArticleById(Integer id) {
        return topicArticleService.getArticleById(id);
    }

    /**分页获取最新话题**/
    @ApiOperation("分页获取最新话题")
    @RequestMapping(value = "/listTopicArticle",method = RequestMethod.POST)
    public Result<PageVO<TopicArticleVO>> listTopicArticle(@RequestBody PageArg arg) {
        arg.validate();
        return topicArticleService.listTopicArticle(arg);
    }

    /**获取每日一句**/
    @ApiOperation("获取每日一句")
    @RequestMapping(value = "/getDailySentence",method = RequestMethod.GET)
    public Result<DailySentence> getDailySentence() {
        List<DailySentence> dailySentenceList = dailySentenceRepository.findDailySentenceByNowStatus(1);
        if (dailySentenceList.size() == 0){
            return Result.newError("系统错误");
        }
        return Result.newSuccess(dailySentenceList.get(0));
    }
}
