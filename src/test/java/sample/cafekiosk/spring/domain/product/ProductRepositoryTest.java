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
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.product.infra.ProductRepositoryImpl;


@Import(ProductRepositoryImpl.class)
@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

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

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProductNumber() {

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

        productRepository.saveAll(List.of(product,product2));

        //when
        String latestProductNum = productRepository.findLatestProduct();

        //then
        assertThat(latestProductNum)
                .isEqualTo("002");

    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {


        //when
        String latestProductNum = productRepository.findLatestProduct();

        //then
        assertThat(latestProductNum)
                .isNull();

    }
}