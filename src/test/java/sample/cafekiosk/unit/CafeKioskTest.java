package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

class CafeKioskTest {

    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0)).isInstanceOf(Americano.class);
    }

    @Test
    void addSeveralBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(),2);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

    }

    @Test
    void addSeveralBeveragesWithNegativeCount() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 한 잔 이상");
    }

    @Test
    void remove() {
    }

    @Test
    void clear() {
    }

    @Test
    void calculateTotalPrice() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        Latte latte = new Latte();
        cafeKiosk.add(latte);

        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(9000);
    }

    @Test
    void createOrder() {

        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        Order order = cafeKiosk.createOrder();
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0)).isEqualTo(americano);


    }

    @Test
    void createOrderWithCurrentDateTime() {

        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2021, 1, 1, 19, 0)))
                .isInstanceOf(IllegalArgumentException.class);


    }
}