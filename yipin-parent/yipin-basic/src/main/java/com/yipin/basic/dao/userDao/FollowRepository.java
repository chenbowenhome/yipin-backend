package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Integer> {
    Follow findFollowByUserIdAndFollowUserId(Integer userId,Integer followUserId);
    Page<Follow> findFollowByUserIdOrderByCreateTimeDesc(Integer userId, Pageable pageable);
    Page<Follow> findFollowByFollowUserIdOrderByCreateTimeDesc(Integer followUserId,Pageable pageable);
}
