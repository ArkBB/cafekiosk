package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

class OrderTest {

    @DisplayName("상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        //given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );


        //when
        Order order = Order.create(products, LocalDateTime.now());


        //then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
    @Test
    void createProduct() {
        //given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );


        //when
        Order order = Order.create(products, LocalDateTime.now());


        //then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registerdDateTIme() {
        //given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );


        //when
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Order order = Order.create(products, registeredDateTime);


        //then
        assertThat(order.getOrderTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(String productNumber, int price)
    {
        return Product.builder()
                .type(ProductType.HANDMADE)
                .price(price)
                .productNumber(productNumber)
                .build();
    }
}