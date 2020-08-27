package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.UserArt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserArtRepository extends JpaRepository<UserArt,Integer> {
    @Query(value = "SELECT * FROM user_art WHERE user_id = ?1 ORDER BY create_time DESC LIMIT 1",nativeQuery = true)
    UserArt findLastUserArt(Integer userId);
}
