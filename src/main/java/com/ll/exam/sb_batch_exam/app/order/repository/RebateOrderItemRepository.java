package com.ll.exam.sb_batch_exam.app.order.repository;

import com.ll.exam.sb_batch_exam.app.order.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {
    Optional<RebateOrderItem> findByOrderItemId(Long orderItemId);
}
