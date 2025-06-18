package sample.cafekiosk.spring.domain.product.infra;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@AllArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository{

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product findByIdOrThrow(Long id) {
        return jpaProductRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product Not Found"));
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return jpaProductRepository.saveAll(products);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }

    @Override
    public List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes) {
        return jpaProductRepository.findAllBySellingStatusIn(sellingTypes);
    }

    @Override
    public List<Product> findAllByProductNumberIn(List<String> productNumbers) {
        return jpaProductRepository.findAllByProductNumberIn(productNumbers);
    }

    @Override
    public void deleteAllInBatch() {
        jpaProductRepository.deleteAllInBatch();
    }

}
