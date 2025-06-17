package sample.cafekiosk.spring.domain.product.infra;


import java.util.List;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

public interface ProductRepository{

    Product findByIdOrThrow(Long id);
    Product save(Product product);
    List<Product> saveAll(List<Product> products);
    List<Product> findAll();
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);
    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    void deleteAllInBatch();
}
