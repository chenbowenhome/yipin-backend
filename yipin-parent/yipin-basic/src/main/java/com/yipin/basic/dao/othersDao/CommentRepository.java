package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentById(Integer id);
    Page<Comment> findCommentByProductionIdAndMainCommentIdOrderByLikesDesc(Integer productionId,Integer mainCommentId,Pageable pageable);
    Page<Comment> findCommentByUserIdOrderByLikesDesc(Integer userId,Pageable pageable);
    @Query(value = "SELECT * FROM comment WHERE main_comment_id=? ORDER BY likes DESC LIMIT 4",nativeQuery = true)
    List<Comment> findCommentByMainCommentIdOrderByLikesDesc(Integer mainCommentId);
    Page<Comment> findCommentByMainCommentIdOrderByLikesDesc(Integer mainCommentId,Pageable pageable);
    Page<Comment> findCommentByProductionIdOrderByLikesDesc(Integer productionId,Pageable pageable);

    Page<Comment> findCommentByTopicArticleIdOrderByLikesDesc(Integer topicArticleId,Pageable pageable);
    Page<Comment> findCommentByTopicArticleIdAndMainCommentIdOrderByLikesDesc(Integer productionId,Integer mainCommentId,Pageable pageable);
}
