package sample.cafekiosk.spring.domain.product.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductSellingStatus sellingType;
    private ProductType type;
    private String name;
    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductSellingStatus sellingType, ProductType type, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.sellingType = sellingType;
        this.type = type;
        this.name = name;
        this.price = price;
    }


    public static ProductResponse of(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .sellingType(product.getSellingStatus())
                .type(product.getType())
                .name(product.getName())
                .price(product.getPrice())
                .build();

    }
}
