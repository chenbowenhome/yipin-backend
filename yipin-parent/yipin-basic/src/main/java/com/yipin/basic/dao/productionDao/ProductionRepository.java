package com.yipin.basic.dao.productionDao;


import com.yipin.basic.entity.production.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductionRepository extends JpaRepository<Production,Integer> {
    List<Production> findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(Integer publish_status,Integer user_id);
    Production findProductionById(Integer id);
    Page<Production> findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(Integer publish_status,Integer user_id,Pageable pageable);
    @Query(value = "SELECT * FROM production p,user u WHERE p.id = u.main_production_id ORDER BY p.create_time DESC",nativeQuery = true)
    Page<Production> findProduction(Pageable pageable);
    @Query(value = "SELECT * FROM production p,user u WHERE p.id = u.main_production_id AND p.title LIKE ? ORDER BY p.create_time DESC",nativeQuery = true)
    Page<Production> findProductionByTitleLikes(String title,Pageable pageable);
    Page<Production> findProductionByOrderByCreateTimeDesc(Pageable pageable);
    Page<Production> findProductionByTitleLike(String title,Pageable pageable);
    List<Production> findProductionByPublishStatusAndUserIdAndEvaluateStatusOrderByCreateTimeDesc(Integer publishStatus,Integer userId,Integer evaluateStatus);
}
