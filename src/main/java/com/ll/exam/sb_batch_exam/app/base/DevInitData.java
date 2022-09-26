package com.ll.exam.sb_batch_exam.app.base;

import com.ll.exam.sb_batch_exam.app.cart.service.CartService;
import com.ll.exam.sb_batch_exam.app.member.entity.Member;
import com.ll.exam.sb_batch_exam.app.member.service.MemberService;
import com.ll.exam.sb_batch_exam.app.order.service.OrderService;
import com.ll.exam.sb_batch_exam.app.product.entity.Product;
import com.ll.exam.sb_batch_exam.app.product.entity.ProductOption;
import com.ll.exam.sb_batch_exam.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;


@Configuration
@Profile("dev")
public class DevInitData {
    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService, CartService cartService, OrderService orderService) {
        return args ->
        {
            String password = "{noop}1234";
            Member member1 = memberService.join("user1", password, "user1@test.com");
            Member member2 = memberService.join("user2", password, "user2@test.com");
            Member member3 = memberService.join("user3", password, "user3@test.com");
            Member member4 = memberService.join("user4", password, "user4@test.com");

            Product product1 = productService.create("단가라 OPS", 68000, "청평화 A-1-15", Arrays.asList(new ProductOption("RED", "44"), new ProductOption("RED", "55"), new ProductOption("BLUE", "44"), new ProductOption("BLUE", "55")));
            Product product2 = productService.create("쉬폰 OPS", 72000, "청평화 A-1-15", Arrays.asList(new ProductOption("BLACK", "44"), new ProductOption("BLACK", "55"), new ProductOption("WHITE", "44"), new ProductOption("WHITE", "55")));

            ProductOption productOption__RED_44 = product1.getProductOptions().get(0);
            ProductOption productOption__BLUE_44 = product1.getProductOptions().get(2);

            cartService.addItem(member1, productOption__RED_44, 1); // productOption__RED_44 총 수량 1
            cartService.addItem(member1, productOption__RED_44, 2); // productOption__RED_44 총 수량 3
            cartService.addItem(member1, productOption__BLUE_44, 1); // productOption__BLUE_44 총 수량 1

            orderService.createFromCart(member1);
        };
    }
}
