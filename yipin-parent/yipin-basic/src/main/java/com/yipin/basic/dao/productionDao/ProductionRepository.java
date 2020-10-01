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
    Page<Production> findProductionByPublishStatusAndUserIdAndDeleteStatusOrderByCreateTimeDesc(Integer publish_status,Integer user_id,Integer deleteStatus,Pageable pageable);
    /**获取首页作品信息**/
    @Query(value = "SELECT * FROM production WHERE is_main_production=1 AND delete_status=0 ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductions(Pageable pageable);
    /**根据标题查询作品信息**/
    @Query(value = "SELECT * FROM production WHERE is_main_production_id=1 AND delete_status=0 AND title LIKE ? ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductionByTitleLikes(String title,Pageable pageable);
    Page<Production> findProductionByOrderByCreateTimeDesc(Pageable pageable);
    List<Production> findProductionByPublishStatusAndUserIdAndEvaluateStatusOrderByCreateTimeDesc(Integer publishStatus,Integer userId,Integer evaluateStatus);
    /**查询代表作信息**/
    List<Production> findProductionByIsMainProductionAndUserIdOrderByCreateTimeDesc(Integer isMainProduction,Integer userId);
    /**根据标题或者描述查询作品**/
    @Query(value = "SELECT * FROM production WHERE (title LIKE ?1 OR description LIKE ?1) AND delete_status=0 ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductionByKeyLikes(String key,Pageable pageable);
}
