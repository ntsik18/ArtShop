package com.ecommerce.ArtShop.DTO.OrderDTUO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record OrderRequest(

        @NotBlank
        String addressLine,
        @NotBlank
        String city,
        @NotBlank
        String country,
        @NotBlank
        String paymentOption,
        @NotEmpty
        Map<Long, Integer> paintingIdQuantity,
        @NotNull
        CardDetails cardDetails
) {
}
