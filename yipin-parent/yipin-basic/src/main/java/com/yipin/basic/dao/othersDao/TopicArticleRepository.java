package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.TopicArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicArticleRepository extends JpaRepository<TopicArticle,Integer> {
    TopicArticle findTopicArticleById(Integer id);
    Page<TopicArticle> findTopicArticleByOrderByCreateTimeDesc(Pageable pageable);
}
