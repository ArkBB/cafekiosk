package sample.cafekiosk.spring.domain.product.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

public interface JpaProductRepository extends JpaRepository<Product, Long> {


    /*
    select *
    from product
    where selling_type in ('SELLING', 'HOLD')
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);


    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
