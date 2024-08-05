package com.ecommerce.ArtShop.DTO.PaintingDTO;

import lombok.*;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NewPaintingResponse {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;
    private String owner;
    private String images;

}
