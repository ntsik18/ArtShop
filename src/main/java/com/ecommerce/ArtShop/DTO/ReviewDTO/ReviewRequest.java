package com.ecommerce.ArtShop.DTO.ReviewDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReviewRequest(

        @NotNull
        String comment,
        @NotNull
        Long paintingId
) {
}
