package com.yipin.basic.dao.order;

import com.yipin.basic.entity.order.ArtProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ArtProduct,String> {
    ArtProduct findArtProductById(String id);
    @Query(value = "SELECT * FROM art_product WHERE id IN (?1) ORDER BY create_time DESC",nativeQuery = true)
    List<ArtProduct> findArtProductByIds(List<String> ids);
}
