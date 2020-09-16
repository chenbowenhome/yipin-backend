package com.yipin.basic.dao.userDao;


import com.yipin.basic.entity.user.UserPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPerformanceRepository extends JpaRepository<UserPerformance,Integer> {
    @Query(value = "SELECT * FROM user_performance WHERE user_id = ?1 ORDER BY create_time DESC LIMIT 1",nativeQuery = true)
    UserPerformance findLastUserPerformance(Integer userId);
    @Query(value = "SELECT comment_nums FROM user_performance WHERE is_period = 1 ORDER BY comment_nums DESC LIMIT 1",nativeQuery = true)
    Integer findMaxCommentNums();
    @Query(value = "SELECT like_nums FROM user_performance WHERE is_period = 1 ORDER BY like_nums DESC LIMIT 1",nativeQuery = true)
    Integer findMaxLikeNums();
    @Query(value = "SELECT exceptional_amount FROM user_performance WHERE is_period = 1 ORDER BY exceptional_amount DESC LIMIT 1",nativeQuery = true)
    Integer findMaxExceptionalAmount();
    @Query(value = "SELECT reward_amount FROM user_performance WHERE is_period = 1 ORDER BY reward_amount DESC LIMIT 1",nativeQuery = true)
    Integer findMaxRewardAmount();
    @Query(value = "SELECT * FROM user_performance WHERE user_id = ?1 AND is_period <> 1 ORDER BY create_time DESC LIMIT 1",nativeQuery = true)
    UserPerformance findSecondUserPerformance(Integer userId);
}
