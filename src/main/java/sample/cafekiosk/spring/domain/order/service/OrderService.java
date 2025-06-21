
package sample.cafekiosk.spring.domain.order.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /*
    * 재고 감소 -> 동시성 고민
    * optimistic lock / pessimistic lock / 고민 필요
    */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registered) {

        List<String> productNumbers = request.getProductNumbers();

        List<Product> products = findProductsBy(productNumbers);

        // 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());

        // 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));

        // 상품별 couting
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // 재고 차감 시도
        for(Stock stock : stockMap.values()) {

            int quantity = productCountingMap.get(stock.getProductNumber()).intValue();
            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
           // stockRepository.save(stock);

        }
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