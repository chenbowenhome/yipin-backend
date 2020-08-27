package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByMobile(String mobile);
    User findUserById(Integer id);
    User findUserByOpenid(String openid);
    @Query(value = "SELECT * FROM user ORDER BY performance_num DESC LIMIT 3",nativeQuery = true)
    List<User> findTheTopThree();
    @Query(value = "SELECT id FROM user",nativeQuery = true)
    List<Integer> findUserId();
}
