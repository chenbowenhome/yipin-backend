package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.VO.TopicArticleVO;
import com.yipin.basic.dao.othersDao.TopicArticleRepository;
import com.yipin.basic.entity.others.TopicArticle;
import com.yipin.basic.form.TopicArticleForm;
import com.yipin.basic.service.TopicArticleService;
import com.yipin.basic.utils.MarkdownUtils;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TopicArticleServiceImpl implements TopicArticleService {

    @Autowired
    private TopicArticleRepository topicArticleRepository;

    /**通过id获取话题文章**/
    @Override
    public Result<TopicArticle> getArticleById(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        TopicArticle topicArticle = topicArticleRepository.findTopicArticleById(id);
        if (topicArticle == null){
            return Result.newError("文章不存在");
        }
        return Result.newSuccess(topicArticle);
    }

    /**上传话题**/
    @Override
    public Result<Void> addTopicArticle(TopicArticleForm topicArticleForm) {
        TopicArticle topicArticle = new TopicArticle();
        BeanUtils.copyProperties(topicArticleForm,topicArticle);
        topicArticle.setUpdateTime(new Date());
        topicArticle.setCreateTime(new Date());
        topicArticle.setComments(0);
        topicArticle.setLikes(0);
        topicArticle.setViews(0);
        topicArticleRepository.save(topicArticle);
        return Result.newSuccess();
    }

    /**分页获取最新话题**/
    @Override
    public Result<PageVO<TopicArticleVO>> listTopicArticle(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<TopicArticle> topicArticlePage = topicArticleRepository.findTopicArticleByOrderByCreateTimeDesc(pageable);
        List<TopicArticle> topicArticleList = topicArticlePage.getContent();
        List<TopicArticleVO> topicArticleVOList = new ArrayList<>();
        for (TopicArticle topicArticle : topicArticleList) {
            TopicArticleVO topicArticleVO = new TopicArticleVO();
            BeanUtils.copyProperties(topicArticle,topicArticleVO);
            topicArticleVOList.add(topicArticleVO);
        }
        //构建pageVo对象
        PageVO<TopicArticleVO> pageVo = PageVO.<TopicArticleVO>builder()
                .totalPage(topicArticlePage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(topicArticleVOList)
                .build();
        return Result.newSuccess(pageVo);
    }
}
