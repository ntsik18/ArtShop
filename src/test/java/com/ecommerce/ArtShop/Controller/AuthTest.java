package com.ecommerce.ArtShop.Controller;

import com.ecommerce.ArtShop.TestContainerConfig.TestContainerConfig;
import com.ecommerce.ArtShop.Repository.TokenRepository;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthTest extends TestContainerConfig {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TokenRepository tokenRepository;



    @Test
    public void testUserRegistrationIntegration() throws Exception {
        // Arrange
        String userJson = "{\"email\": \"test@example.com\", \"password\": \"password123\", \"firstName\": \"example\"," +
                " \"lastName\": \"example\"}";

        // Act
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isAccepted());

        // Assert - You might verify that the registration is persisted in the database
        assertThat(userRepository.findByEmail("test@example.com")).isNotNull();
        Optional<User> user = userRepository.findByEmail("test@example.com");
        assertThat(tokenRepository.findByUser(user)).isNotNull();

        // Retrieve the token generated for the user
        String token = tokenRepository.findByUser(user)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Token not found"))
                .getToken();

        // Act - Account Activation
        mockMvc.perform(get("/auth/activation")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert - Verify that the user's account is activated
        user = userRepository.findByEmail("test@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().isEnabled()).isTrue();

        String authJson = "{\"email\": \"test@example.com\", \"password\": \"password123\"}";
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                .andExpect(status().isOk());
    }

}