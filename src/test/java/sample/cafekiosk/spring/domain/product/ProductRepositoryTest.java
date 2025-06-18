package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.product.infra.ProductRepositoryImpl;

@ActiveProfiles("test") // yml 파일에 on-profile : test 기반으로 작동
//@SpringBootTest
@DataJpaTest
@Import(ProductRepositoryImpl.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn() {

        //given
        Product product = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라뗴")
                .price(4500)
                .build();

        product = productRepository.save(product);
        product2 = productRepository.save(product2);
        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                // 검증하고자 하는 필드만 추출
                .containsExactly(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라뗴",HOLD)
                );
    }

    @Test
    @DisplayName("상품번호들로 상품들을 조회한다.")
    void findAllByProductNumberIn() {

        //given
        Product product = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라뗴")
                .price(4500)
                .build();

        product = productRepository.save(product);
        product2 = productRepository.save(product2);
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactly(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라뗴",HOLD)
                );
    }
}