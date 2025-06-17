package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
