package com.yipin.basic.dao;

import com.yipin.basic.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentById(Integer id);
    Page<Comment> findCommentByProductionIdOrderByCreateTime(Integer productionId,Pageable pageable);
}
