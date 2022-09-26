package com.ll.exam.sb_batch_exam.app.cart.repository;

import com.ll.exam.sb_batch_exam.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
