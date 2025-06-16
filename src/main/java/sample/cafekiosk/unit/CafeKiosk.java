package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

@Getter
public class CafeKiosk {

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(18, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("음료는 한 잔 이상");
        }

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice;
    }

    public Order createOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();

        if (nowTime.isBefore(START_TIME) || nowTime.isAfter(END_TIME)) {
            throw new IllegalArgumentException("주문 시간이 아닙니다.");
        }

        return new Order(now, beverages);
    }

    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        if(currentTime.isBefore(START_TIME) || currentTime.isAfter(END_TIME)){
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요");
        }

        return new Order(currentDateTime, beverages);
    }

}
