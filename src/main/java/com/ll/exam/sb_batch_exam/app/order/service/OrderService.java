package com.ll.exam.sb_batch_exam.app.order.service;

import com.ll.exam.sb_batch_exam.app.cart.entity.CartItem;
import com.ll.exam.sb_batch_exam.app.cart.service.CartService;
import com.ll.exam.sb_batch_exam.app.member.entity.Member;
import com.ll.exam.sb_batch_exam.app.member.service.MemberService;
import com.ll.exam.sb_batch_exam.app.order.entity.Order;
import com.ll.exam.sb_batch_exam.app.order.entity.OrderItem;
import com.ll.exam.sb_batch_exam.app.order.repository.OrderRepository;
import com.ll.exam.sb_batch_exam.app.product.entity.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;


    @Transactional
    public Order createFromCart(Member member) {
        // 입력된 회원의 장바구니 아이템들을 전부 가져온다.

        // 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
        // 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제

        List<CartItem> cartItems = cartService.getItemsByMember(member);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            ProductOption productOption = cartItem.getProductOption();

            if (productOption.isOrderable(cartItem.getQuantity())) {
                orderItems.add(new OrderItem(productOption, cartItem.getQuantity()));
            }

            cartService.deleteItem(cartItem);
        }

        return create(member, orderItems);
    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .member(member)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);

        return order;
    }

    @Transactional // 서비스 클래스에 @Transactional(readOnly = true) 붙여서 @Transactional을 따로 안붙이면 디비에 반영안됨
    public void payByRestCashOnly(Order order) {
        Member orderer = order.getMember();

        long restCash = orderer.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(orderer, payPrice * -1, "주문결제__예치금결제");

        order.setPaymentDone();
        orderRepository.save(order);
    }

    // 서비스 레이어에서 환불 처리
    // - 예치금 환불
    // - refundDone 상태(this.refundQuantity = quantity; this.refundPrice = payPrice;)
    // - DB 저장
    @Transactional
    public void refund(Order order) {
        int payPrice = order.getPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문환불__예치금환불");

        order.setRefundDone();
        orderRepository.save(order);
    }
}
