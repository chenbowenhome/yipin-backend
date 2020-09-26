package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Integer> {
    Likes findLikesByUserIdAndProductionId(Integer userId,Integer productionId);
    Likes findLikesByUserIdAndCommentId(Integer userId,Integer commentId);
}
