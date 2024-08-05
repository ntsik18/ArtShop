package com.ecommerce.ArtShop.Controller;


import com.ecommerce.ArtShop.DTO.OrderDTUO.CardDetails;
import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderRequest;
import com.ecommerce.ArtShop.MockUser.WithMockUser;
import com.ecommerce.ArtShop.TestContainerConfig.TestContainerConfig;
import com.ecommerce.ArtShop.Repository.OrderRepository;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaintingRepository paintingRepository;

    @Test
    @WithMockUser
    @Sql("/Order.sql")
    public void testOrder() throws Exception {
        Map<Long, Integer> paintingIdQuantity = new HashMap<>();
        paintingIdQuantity.put(1L, 2);
        paintingIdQuantity.put(2L, 1);
        CardDetails cardDetails = new CardDetails("1234567812345678", "09/27", "123");
        OrderRequest request = new OrderRequest("test", "test", "test", "Visa", paintingIdQuantity, cardDetails);
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        assertThat(orderRepository.findById(1L)).isNotEmpty();
        assertThat(paintingRepository.findById(1L).get().getQuantity()).isEqualTo(0);
        assertThat(paintingRepository.findById(2L).get().getQuantity()).isEqualTo(0);

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk());  // This line will not be reached if an exception is thrown
        });


    }
}
