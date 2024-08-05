package com.ecommerce.ArtShop.DTO.PaintingDTO;

import jakarta.validation.constraints.NotNull;


public record PaintingRequest(


        @NotNull
        String description,
        @NotNull
        String name,
        @NotNull
        Double price,
        @NotNull
        Integer quantity
) {
}
