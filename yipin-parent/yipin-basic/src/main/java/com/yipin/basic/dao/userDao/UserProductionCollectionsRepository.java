package com.yipin.basic.dao.userDao;

import com.yipin.basic.entity.user.UserProductionCollections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProductionCollectionsRepository extends JpaRepository<UserProductionCollections,Integer> {
    UserProductionCollections findUserProductionCollectionsByUserIdAndProductionId(Integer userId,Integer productionId);
}
