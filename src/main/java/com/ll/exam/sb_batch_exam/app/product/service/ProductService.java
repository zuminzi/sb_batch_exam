package com.ll.exam.sb_batch_exam.app.product.service;

import com.ll.exam.sb_batch_exam.app.product.entity.Product;
import com.ll.exam.sb_batch_exam.app.product.entity.ProductOption;
import com.ll.exam.sb_batch_exam.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(String name, int salePrice, int wholesalePrice, String makerShopName, List<ProductOption> options) {
        // Math.ceil->소수점단위 나올 시 올림, /100*100-> 10원 단위 내림해주기 위해
        // ex. 15520/100 = 155 * 100 = 15500 -> 10원단위 내림해줌
        int price = (int) Math.ceil(wholesalePrice * 1.6) / 100 * 100;
        Product product = Product.builder()
                .name(name)
                .salePrice(salePrice)
                .price(price)
                .wholesalePrice(wholesalePrice)
                .makerShopName(makerShopName)
                .build();



        for ( ProductOption option : options ) {
            product.addOption(option);
        }

        productRepository.save(product);

        return product;
    }
}
