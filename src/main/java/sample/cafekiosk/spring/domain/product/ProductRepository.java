package sample.cafekiosk.spring.domain.product;


import java.util.List;

public interface ProductRepository{

    Product findByIdOrThrow(Long id);
    Product save(Product product);
    List<Product> findAll();
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);
}
