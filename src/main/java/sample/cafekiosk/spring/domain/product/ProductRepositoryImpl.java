package sample.cafekiosk.spring.domain.product;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }

    @Override
    public List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes) {
        return jpaProductRepository.findAllBySellingStatusIn(sellingTypes);
    }

}
