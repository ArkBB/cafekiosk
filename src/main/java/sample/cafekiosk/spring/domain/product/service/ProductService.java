package sample.cafekiosk.spring.domain.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public List<ProductResponse> getSellingProducts() {

        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());

    }

    // 동시성 이슈 때문에
    // 실제 서비스에서는 UUID 활용하는게 좋을 수도
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String latestProductNumber = productRepository.findLatestProduct();

        Product product = request.toEntity(request,createNextProductNumber(latestProductNumber));

        Product savedProduct = productRepository.save(product);

        ProductResponse productResponse = ProductResponse.of(savedProduct);

        return productResponse;
    }

    private String createNextProductNumber(String latestProductNumber) {
        if (latestProductNumber == null) {
            return "001";
        }
        int nextNumber = Integer.parseInt(latestProductNumber) + 1;
        return String.format("%03d", nextNumber);
    }
}
