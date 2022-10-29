package com.ll.exam.sb_batch_exam.app.order.repository;

import com.ll.exam.sb_batch_exam.app.order.entity.Order;
import com.ll.exam.sb_batch_exam.app.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<Order, Long> {
    Page<OrderItem> findAllByIdLessThan(long id, Pageable pageable);
    Page<OrderItem> findAllByIdBetween(long fromId, long toId, Pageable pageable);
}

