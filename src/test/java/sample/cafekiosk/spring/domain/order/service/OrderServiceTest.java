package sample.cafekiosk.spring.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;


@SpringBootTest
//@Transactional
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

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        // 지워주는 순서도 중요하다!!
        productRep.deleteAllInBatch();
        orderRep.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {

        //given
        Product product = createProduct(HANDMADE, "001", 4000);
        Product product2 = createProduct(HANDMADE, "002", 4500);
        Product product3 = createProduct(HANDMADE, "003", 7000);

        productRep.saveAll(List.of(product,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.
                builder().productNumbers(List.of("001","003")).build();

        //when
        LocalDateTime registerdDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, registerdDateTime);
        //then
        assertThat(orderResponse.getId()).isNotNull();
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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE,"001",1000);
        Product product2 = createProduct(BAKERY,"002",3000);
        Product product3 = createProduct(HANDMADE,"003",5000);

        productRep.saveAll(List.of(product1,product2,product3));
        // when

        Stock stock1 = Stock.create("001",2);
        Stock stock2 = Stock.create("002",1);
        Stock stock3 = Stock.create("003",3);

        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001","002","003"))
                .build();

        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
        // then
        assertThat(orderResponse)
                .extracting("totalPrice","orderTime")
                .contains(10000, registeredDateTime);


        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("ProductNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(3)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",0),
                        tuple("002",0),
                        tuple("003",3)
                );

    }

    @DisplayName("재고가 부족한 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")

    @Test
    void createOrderWithStock2() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE,"001",1000);
        Product product2 = createProduct(BAKERY,"002",3000);
        Product product3 = createProduct(HANDMADE,"003",5000);

        productRep.saveAll(List.of(product1,product2,product3));
        // when

        Stock stock1 = Stock.create("001",2);
        Stock stock2 = Stock.create("002",0);
        Stock stock3 = Stock.create("003",3);

        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001","002","003"))
                .build();


        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("재고가 부족한 상품이 있습니다.");


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
                .sellingStatus(SELLING)
                .name(productType.name()+"_"+productNumber)
                .build();
    }

    
}