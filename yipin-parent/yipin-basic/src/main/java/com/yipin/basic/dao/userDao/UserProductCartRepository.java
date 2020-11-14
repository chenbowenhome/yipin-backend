package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.UserProductCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductCartRepository extends JpaRepository<UserProductCart,Integer> {
    Page<UserProductCart> findUserProductCartByUserIdAndIsEndOrderByUpdateTimeDesc(Integer userId,Integer isEnd,Pageable pageable);
    UserProductCart findUserProductCartById(Integer id);
}
