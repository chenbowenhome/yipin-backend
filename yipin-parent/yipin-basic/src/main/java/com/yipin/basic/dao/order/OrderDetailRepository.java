package com.yipin.basic.dao.order;

import com.yipin.basic.entity.order.ArtOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<ArtOrderDetail,String> {
    ArtOrderDetail findArtOrderDetailById(String id);
    List<ArtOrderDetail> findArtOrderDetailByOrderId(String orderId);
}
