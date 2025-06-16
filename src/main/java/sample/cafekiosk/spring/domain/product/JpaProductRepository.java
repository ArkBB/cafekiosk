package sample.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {


    /*
    select *
    from product
    where selling_type in ('SELLING', 'HOLD')
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

}
