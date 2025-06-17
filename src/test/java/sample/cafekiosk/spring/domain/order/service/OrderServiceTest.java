package sample.cafekiosk.spring.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;


@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRep;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRep;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        // 지워주는 순서도 중요하다!!
        productRep.deleteAllInBatch();
        orderRep.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {

        //given
        Product product = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라뗴")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRep.saveAll(List.of(product,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.
                builder().productNumbers(List.of("001","003")).build();

        //when
        LocalDateTime registerdDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, registerdDateTime);
        //then
        assertThat(orderResponse)
                .extracting("totalPrice","orderTime")
                        .contains(11000, registerdDateTime);


        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("ProductNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("003", 7000)
                );

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE,"001",1000);
        Product product2 = createProduct(HANDMADE,"002",3000);
        Product product3 = createProduct(HANDMADE,"003",5000);

        productRep.saveAll(List.of(product1,product2,product3));
        // when

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001"))
                .build();

        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
        // then
        assertThat(orderResponse)
                .extracting("totalPrice","orderTime")
                .contains(2000, registeredDateTime);


        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("ProductNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );

    }

    private Product createProduct(ProductType productType,String productNumber, int price)
    {
        return Product.builder()
                .type(productType)
                .price(price)
                .productNumber(productNumber)
                .build();
    }


}