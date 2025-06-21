
package sample.cafekiosk.spring.domain.order.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.domain.order.controller.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.service.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request){

        LocalDateTime registeredDateTime = LocalDateTime.now();
        return ApiResponse.of(HttpStatus.OK,orderService.createOrder(request.toServcieOrderRequest(), registeredDateTime));

    }

}