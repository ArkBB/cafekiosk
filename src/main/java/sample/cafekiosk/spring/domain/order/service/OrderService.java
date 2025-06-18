
package sample.cafekiosk.spring.domain.order.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registered) {

        List<String> productNumbers = request.getProductNumbers();

        List<Product> products = findProductsBy(productNumbers);

        // 재고 차감 체크가 필요한 상품들 filter
        products.stream()
                .filter(product -> ProductType.containStockType(product.getType()));
        // 재고 엔티티 조회
        // 상품별 couting
        // 재고 차감 시도

        Order order = Order.create(products,registered);

        Order savedOrder = orderRepository.save(order);
        // 저장이 된 후에야 Order에 id값 부여됨
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        //in 절이기에 중복제거가 되어 하나만 결과로 나오게 됨.
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, product -> product));

        List<Product> duplicateProducts = productNumbers.stream()
                .map(key -> productMap.get(key))
                .collect(Collectors.toList());
        return duplicateProducts;
    }
}