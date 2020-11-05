package com.yipin.basic.dao.order;

import com.yipin.basic.entity.order.ArtOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ArtOrder,String> {
    ArtOrder findArtOrderById(String id);
    Page<ArtOrder> findArtOrderByUserIdAndPayStatusOrderByCreateTimeDesc(Integer userId, Integer payStatus, Pageable pageable);
}
