package com.yipin.basic.dao.userDao;


import com.yipin.basic.entity.user.UserPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPerformanceRepository extends JpaRepository<UserPerformance,Integer> {
    @Query(value = "SELECT * FROM user_performance WHERE user_id = ?1 ORDER BY create_time DESC LIMIT 1",nativeQuery = true)
    UserPerformance findLastUserPerformance(Integer userId);
}
