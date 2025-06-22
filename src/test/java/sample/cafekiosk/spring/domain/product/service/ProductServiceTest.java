package sample.cafekiosk.spring.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;


class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {

        // given
        Product product1 = createProduct("001", ProductType.HANDMADE,ProductSellingStatus.SELLING, "아메리카노",4000);
        Product product2 = createProduct("002", ProductType.HANDMADE,ProductSellingStatus.HOLD, "카페라떼",4500);
        Product product3 = createProduct("003", ProductType.HANDMADE,ProductSellingStatus.STOP_SELLING,"팥빙수",7000);

        productRepository.saveAll(List.of(product1,product2,product3));
        // when

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5500)
                .build();

        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(productResponse)
                .extracting("productNumber" ,"type","sellingStatus","name","price")
                .contains("004",ProductSellingStatus.SELLING,ProductType.HANDMADE,"카푸치노",5500);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(4)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                        tuple("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500),
                        tuple("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000),
                        tuple("004", ProductType.HANDMADE,
                                ProductSellingStatus.SELLING, "카푸치노", 5500)
                );



    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProductWhenNull() {

        // given

        // when

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5500)
                .build();

        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(productResponse)
                .extracting("productNumber" ,"type","sellingStatus","name","price")
                .contains("001",ProductSellingStatus.SELLING,ProductType.HANDMADE,"카푸치노",5500);


    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus productSellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}