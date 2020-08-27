package com.yipin.basic.dao.productionDao;


import com.yipin.basic.entity.production.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductionRepository extends JpaRepository<Production,Integer> {
    Page<Production> findProductionByPublishStatusOrderByCreateTimeDesc(Pageable pageable,Integer publish_status);
    Production findProductionById(Integer id);
    Page<Production> findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(Integer publish_status,Integer user_id,Pageable pageable);
    @Query(value = "SELECT * FROM production p,user u WHERE p.id = u.main_production_id ORDER BY u.performance_num DESC",nativeQuery = true)
    Page<Production> findProduction(Pageable pageable);
    @Query(value = "SELECT * FROM production p,user u WHERE p.id = u.main_production_id AND p.title LIKE ?1 ORDER BY u.performance_num DESC",nativeQuery = true)
    Page<Production> findProductionByTitleLike(String title,Pageable pageable);
}
