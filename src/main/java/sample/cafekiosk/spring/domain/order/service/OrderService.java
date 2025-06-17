
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
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registered) {

        List<String> productNumbers = request.getProductNumbers();

        List<Product> duplicateProducts = findProductsBy(productNumbers);

        Order order = Order.create(duplicateProducts,registered);

        Order savedOrder = orderRepository.save(order);

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