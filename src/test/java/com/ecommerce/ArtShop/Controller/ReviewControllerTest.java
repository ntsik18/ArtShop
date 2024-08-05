package com.ecommerce.ArtShop.Controller;

import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewRequest;
import com.ecommerce.ArtShop.MockUser.WithMockUser;
import com.ecommerce.ArtShop.TestContainerConfig.TestContainerConfig;
import com.ecommerce.ArtShop.Repository.ReviewRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReviewControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepo reviewRepo;

    @Test
    @WithMockUser
    @Sql("/ReviewPainting.sql")
    public void testReview() throws Exception {
        ReviewRequest reviewRequest = ReviewRequest.builder().comment("test").paintingId(1L).build();
        String jsonRequest = objectMapper.writeValueAsString(reviewRequest);
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        assertThat(reviewRepo.findById(1L)).isNotNull();
        assertThat(reviewRepo.findById(1L).get().getUser().getId()).isEqualTo(1L);

    }
}
