package com.ecommerce.ArtShop.DTO.AuthDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
    @Email(message = "Email is not well formatted")
    @NotBlank(message = "Email is mandatory")
    public String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    public String password;

    @NotBlank(message = "Firstname is mandatory")
    public String firstName;


    @NotBlank(message = "Lastname is mandatory")
    public String lastName;

}
