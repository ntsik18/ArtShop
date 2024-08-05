package com.ecommerce.ArtShop.DTO.AuthDTO;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    public String accessToken;
}
