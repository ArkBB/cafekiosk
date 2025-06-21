package sample.cafekiosk.spring.domain.product.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.TestConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;

    static class TestConfig {
        @Bean
        ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        //given
        ProductCreateRequest createRequest = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .name("아메리카노")
                .build();

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        when(productService.createProduct(Mockito.any()))
                        .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .accept( "application/json")
                        .contentType("application/json")

                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수입니다.")
    @Test
    void createProductWithoutType() throws Exception {
        //given
        ProductCreateRequest createRequest = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .name("아메리카노")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(createRequest))
                        .accept( "application/json")
                        .contentType("application/json")

                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        //given
        when(productService.getSellingProducts())
                .thenReturn(List.of());

        //when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());



    }
}
