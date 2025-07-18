package sample.cafekiosk.spring.domain.product;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리");

    private final String text;

    public static boolean containStockType(ProductType type) {

        return List.of(BOTTLE,BAKERY).contains(type);
    }
}
