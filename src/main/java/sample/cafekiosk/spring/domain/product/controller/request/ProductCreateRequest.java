package sample.cafekiosk.spring.domain.product.controller.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sample.cafekiosk.spring.domain.product.service.request.ProductServiceCreateRequest;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다")
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }


    public ProductServiceCreateRequest toServiceRequest() {
        return ProductServiceCreateRequest.builder()
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();

    }
}
