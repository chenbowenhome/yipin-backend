package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.UserMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMoneyRepository extends JpaRepository<UserMoney,Integer> {
    UserMoney findUserMoneyById(Integer id);
    UserMoney findUserMoneyByUserId(Integer userId);
}
