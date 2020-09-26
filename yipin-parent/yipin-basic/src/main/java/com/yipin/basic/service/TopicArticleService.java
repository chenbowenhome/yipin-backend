package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.TopicArticleVO;
import com.yipin.basic.entity.others.TopicArticle;
import com.yipin.basic.form.TopicArticleForm;

public interface TopicArticleService {
    /**通过id获取话题文章**/
    Result<TopicArticle> getArticleById(Integer id);
    /**上传话题**/
    Result<Void> addTopicArticle(TopicArticleForm topicArticleForm);
    /**分页获取最新话题**/
    Result<PageVO<TopicArticleVO>> listTopicArticle(PageArg arg);
}
