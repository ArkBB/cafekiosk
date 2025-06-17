package sample.cafekiosk.spring.domain.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.infra.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts(){

        List<Product> products = productRepository.findAllBySellingStatusIn(
                List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD));
        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());

    }


}
