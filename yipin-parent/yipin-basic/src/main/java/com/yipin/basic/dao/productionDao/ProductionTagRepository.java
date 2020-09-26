package com.yipin.basic.dao.productionDao;

import com.yipin.basic.entity.production.ProductionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionTagRepository extends JpaRepository<ProductionTag,Integer> {
    ProductionTag findProductionTagById(Integer id);
    List<ProductionTag> findProductionTagByOrderByOrderNumAsc();
}
