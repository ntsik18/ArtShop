package com.ecommerce.ArtShop.Controller;


import com.ecommerce.ArtShop.DTO.PaintingDTO.NewPaintingResponse;
import com.ecommerce.ArtShop.Mapper.PaintingMapper;
import com.ecommerce.ArtShop.DTO.PaintingDTO.PaintingRequest;
import com.ecommerce.ArtShop.MockUser.WithMockUser;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.TestContainerConfig.TestContainerConfig;
import com.ecommerce.ArtShop.Model.Painting;
import com.ecommerce.ArtShop.Repository.ImageModelRepo;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import com.ecommerce.ArtShop.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaintingControllerTest extends TestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaintingRepository paintingRepository;

    @Autowired
    private ImageModelRepo imageModelRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaintingMapper paintingMapper;
    @Autowired
    private UserRepository userRepository;



    @Test
    @com.ecommerce.ArtShop.MockUser.WithMockUser
    public void testUploadAndFindByIdAndDelete() throws Exception {
        // Prepare multipart file
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "file content".getBytes());

        // Prepare PaintingRequest
        PaintingRequest paintingRequest = new PaintingRequest("Beautiful painting", "Sunset", 100.0, 1);
        String jsonRequest = objectMapper.writeValueAsString(paintingRequest);
        MockMultipartFile paintingRequestPart = new MockMultipartFile("painting", "", "application/json", jsonRequest.getBytes());

        // Perform request
        mockMvc.perform(multipart("/paintings/new")
                        .file(file)
                        .file(paintingRequestPart))
                .andExpect(status().isOk());

        assertThat(paintingRepository.findById(1L)).isNotEmpty();
        assertThat(imageModelRepo.findById(1L)).isNotEmpty();

        Optional<Painting> painting = paintingRepository.findById(1L);
        NewPaintingResponse paintingResponse = paintingMapper.toNewPaintingResponse(painting.orElse(null));

        mockMvc.perform(get("/paintings/{painting-id}", painting.get().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paintingResponse)));


        mockMvc.perform(delete("/paintings/delete/{id}", 1L))
                .andExpect(status().isOk());
        assertThat(!paintingRepository.findById(1L).isPresent());

        assertThrows(ServletException.class, () -> {
        mockMvc.perform(delete("/paintings/delete/{id}", 100L))
        .andExpect(status().isOk()); });

    }




    @Sql("/data.sql")
    @Test
    public void testSearchPaintings() throws Exception {
//
        ResultActions actions = mockMvc.perform(get("/paintings/search/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.contents", hasSize(5)));
        assertThat(paintingRepository.findById(1L)).isNotEmpty();
        assertThat(userRepository.findByEmail("example@example.com")).isNotEmpty();

        for (int i = 0; i < 5; i++) {
            actions = actions.andExpect(jsonPath("$.contents[" + i + "].id", is(i + 1)));
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/paintings/search/paintings")
                        .param("text", "Abstract")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").exists())
                .andExpect(jsonPath("$.contents", hasSize(2)));

    }

    @Test
    @Sql("/Delete.sql")
    @WithMockUser
    public void testDeleteNotPermitted() throws Exception {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/paintings/delete/{id}", 200L))
                    .andExpect(status().isOk());
            assertThat(!paintingRepository.findById(200L).isPresent()); });

    }


}






