package com.ll.exam.sb_batch_exam.app.base;

import com.ll.exam.sb_batch_exam.app.cart.service.CartService;
import com.ll.exam.sb_batch_exam.app.member.entity.Member;
import com.ll.exam.sb_batch_exam.app.member.service.MemberService;
import com.ll.exam.sb_batch_exam.app.order.entity.Order;
import com.ll.exam.sb_batch_exam.app.order.service.OrderService;
import com.ll.exam.sb_batch_exam.app.product.entity.Product;
import com.ll.exam.sb_batch_exam.app.product.entity.ProductOption;
import com.ll.exam.sb_batch_exam.app.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Slf4j // log 위한 어노테이션
@Configuration
@Profile("dev")
public class DevInitData {
    private boolean initDataDone = false; // 데이터 처리 여부 체크

    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService, CartService cartService, OrderService orderService) {
        return args ->
        {
            if (initDataDone) return;

            initDataDone = true;

            class Helper {
                public Order order(Member member, List<ProductOption> productOptions) {
                    for (int i = 0; i < productOptions.size(); i++) {
                        ProductOption productOption = productOptions.get(i);

                        cartService.addItem(member, productOption, i + 1);
                    }

                    return orderService.createFromCart(member);
                }
            }

            Helper helper = new Helper();
            String password = "{noop}1234";
            Member member1 = memberService.join("user1", password, "user1@test.com");
            Member member2 = memberService.join("user2", password, "user2@test.com");
            Member member3 = memberService.join("user3", password, "user3@test.com");
            Member member4 = memberService.join("user4", password, "user4@test.com");

            // 만원 충전
            memberService.addCash(member1, 10_000, "충전__무통장입금"); // 가독성 위해 자바 숫자 리터럴에 _ 사용가능
            // 이만원 충전
            memberService.addCash(member1, 20_000, "충전__무통장입금");
            // 5천원 사용
            memberService.addCash(member1, -5_000, "출금__일반");
            // 10억 충전
            memberService.addCash(member1, 1_000_000_000, "충전__무통장입금");
            // 현재 보유중인 캐시 금액
            long restCash = memberService.getRestCash(member1);
            log.debug("member1 restCash : " + restCash); // 30000-5000 + 300000

            Product product1 = productService.create("단가라 OPS", 68000, 45000, "청평화 A-1-15", Arrays.asList(new ProductOption("RED", "44"), new ProductOption("RED", "55"), new ProductOption("BLUE", "44"), new ProductOption("BLUE", "55")));
            Product product2 = productService.create("쉬폰 OPS", 72000, 55000, "청평화 A-1-15", Arrays.asList(new ProductOption("BLACK", "44"), new ProductOption("BLACK", "55"), new ProductOption("WHITE", "44"), new ProductOption("WHITE", "55")));

            ProductOption product1Option__RED_44 = product1.getProductOptions().get(0);
            ProductOption product1Option__RED_55 = product1.getProductOptions().get(1);
            ProductOption product1Option__BLUE_44 = product1.getProductOptions().get(2);
            ProductOption product1Option__BLUE_55 = product1.getProductOptions().get(3);

            cartService.addItem(member1, product1Option__RED_44, 1); // productOption__RED_44 총 수량 1
            cartService.addItem(member1, product1Option__RED_44, 2); // productOption__RED_44 총 수량 3
            cartService.addItem(member1, product1Option__BLUE_44, 1); // productOption__BLUE_44 총 수량 1

            // 장바구니 -> 주문
            Order order1 = helper.order(member1, Arrays.asList(
                            product1Option__RED_44,
                            product1Option__RED_44,
                            product1Option__BLUE_44
                    )
            );

            int order1PayPrice = order1.calculatePayPrice();
            orderService.payByRestCashOnly(order1);

            /* 2번 주문 생성 */
            // - 장바구니에 담기
            // - 주문 생성

            ProductOption product2Option__BLACK_44 = product2.getProductOptions().get(0);
            ProductOption product2Option__BLACK_55 = product2.getProductOptions().get(1);
            ProductOption product2Option__WHITE_44 = product2.getProductOptions().get(2);
            ProductOption product2Option__WHITE_55 = product2.getProductOptions().get(3);


            cartService.addItem(member2, product1Option__RED_44, 1);
            cartService.addItem(member2, product2Option__BLACK_44, 2);
            cartService.addItem(member2, product2Option__WHITE_44, 2);

            // 장바구니 아이템 -> 주문
            Order order2 = helper.order(member2, Arrays.asList(
                            product1Option__RED_44,
                            product2Option__BLACK_44,
                            product2Option__WHITE_44
                    )
            );

            log.debug("order2 payPrice : " + order2.calculatePayPrice());

            // 충전
            memberService.addCash(member2, 1_000_000_000, "충전__무통장입금");

            log.debug("member2 restCash : " + member2.getRestCash());

            // 결제
            orderService.payByRestCashOnly(order2);

            // 환불
            orderService.refund(order2);

            Order order3 = helper.order(member2, Arrays.asList(
                            product1Option__RED_44,
                            product1Option__RED_55,
                            product2Option__BLACK_44,
                            product2Option__WHITE_44
                    )
            );
            orderService.payByRestCashOnly(order3);

            Order order4 = helper.order(member1, Arrays.asList(
                            product1Option__RED_44,
                            product2Option__WHITE_44
                    )
            );
            orderService.payByRestCashOnly(order4);
        };
    }
}
