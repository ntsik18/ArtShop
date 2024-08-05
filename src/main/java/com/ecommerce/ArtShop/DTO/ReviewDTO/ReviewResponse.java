package com.ecommerce.ArtShop.DTO.ReviewDTO;

import lombok.*;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private String review;
    private Long painting_Id;
    private String painting_name;
    private Long user_Id;


}
