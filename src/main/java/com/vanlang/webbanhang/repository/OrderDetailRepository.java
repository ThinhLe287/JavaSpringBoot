package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    void deleteByOrderId(Long orderId);
}
