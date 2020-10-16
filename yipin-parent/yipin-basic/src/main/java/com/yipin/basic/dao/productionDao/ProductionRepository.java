package com.yipin.basic.dao.productionDao;


import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.UserProductionCollections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductionRepository extends JpaRepository<Production,Integer> {
    @Query(value = "SELECT * FROM production WHERE delete_status=0 AND publish_status=?1 AND user_id=?2 ORDER BY create_time DESC",nativeQuery = true)
    List<Production> findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(Integer publish_status,Integer user_id);
    Production findProductionById(Integer id);
    Page<Production> findProductionByPublishStatusAndUserIdAndDeleteStatusOrderByCreateTimeDesc(Integer publish_status,Integer user_id,Integer deleteStatus,Pageable pageable);
    /**获取首页作品信息**/
    @Query(value = "SELECT * FROM production WHERE delete_status=0 AND publish_status=1 ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductions(Pageable pageable);
    /**根据标题查询作品信息**/
    @Query(value = "SELECT * FROM production WHERE delete_status=0 AND publish_status=1 AND title LIKE ? ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductionByTitleLikes(String title,Pageable pageable);
    @Query(value = "SELECT * FROM production WHERE delete_status=0 ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductionByOrderByCreateTimeDesc(Pageable pageable);
    List<Production> findProductionByPublishStatusAndUserIdAndEvaluateStatusOrderByCreateTimeDesc(Integer publishStatus,Integer userId,Integer evaluateStatus);
    /**查询代表作信息**/
    List<Production> findProductionByIsMainProductionAndUserIdOrderByCreateTimeDesc(Integer isMainProduction,Integer userId);
    /**根据标题或者描述查询作品**/
    @Query(value = "SELECT * FROM production WHERE (title LIKE ?1 OR description LIKE ?1) AND delete_status=0 AND publish_status=1 ORDER BY create_time DESC",nativeQuery = true)
    Page<Production> findProductionByKeyLikes(String key,Pageable pageable);
    /**获取收藏的作品**/
    @Query(value = "SELECT * FROM production WHERE id IN (SELECT production_id FROM user_production_collections WHERE user_id=?1 ORDER BY create_time DESC) AND delete_status=0",nativeQuery = true)
    Page<Production> findUserProductionCollections(Integer userId, Pageable pageable);
}
