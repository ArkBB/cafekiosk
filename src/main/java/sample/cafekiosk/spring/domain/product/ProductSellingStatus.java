package sample.cafekiosk.spring.domain.product;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductSellingStatus {

    SELLING("판매중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매중지");

    private final String text;

}
