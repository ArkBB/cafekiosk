package sample.cafekiosk.spring.domain.product.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select p.product_number from product p order by p.id desc limit 1",nativeQuery = true)
    String findLatestProductNumber();
}
