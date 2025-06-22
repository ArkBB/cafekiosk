package sample.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.domain.order.controller.OrderController;
import sample.cafekiosk.spring.domain.order.service.OrderService;
import sample.cafekiosk.spring.domain.product.controller.ProductController;
import sample.cafekiosk.spring.domain.product.service.ProductService;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;

}
