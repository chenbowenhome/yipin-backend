package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentById(Integer id);
    Page<Comment> findCommentByProductionIdOrderByLikesDesc(Integer productionId,Pageable pageable);
    Page<Comment> findCommentByUserIdOrderByLikesDesc(Integer userId,Pageable pageable);
    Page<Comment> findCommentByActivityIdOrderByLikesDesc(Integer topicArticleId,Pageable pageable);
}
