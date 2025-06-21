package sample.cafekiosk.spring.domain.product.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.domain.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.service.ProductService;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request){

        return ApiResponse.of(HttpStatus.OK,productService.createProduct(request));

    }

    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts(){
        return ApiResponse.of(HttpStatus.OK,productService.getSellingProducts());
    }



}
