package sample.cafekiosk.spring.domain.order.service.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime orderTime;
    private List<ProductResponse> products = new ArrayList<>();

    @Builder
    private OrderResponse(Long id, int totalPrice, LocalDateTime orderTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
        this.products = products;
    }


    public static OrderResponse of(Order order) {
        return OrderResponse.builder().
                id(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderTime(order.getOrderTime())
                .products(order.getOrderProducts().stream()
                        .map(OrderProduct::getProduct)
                        .map(ProductResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
