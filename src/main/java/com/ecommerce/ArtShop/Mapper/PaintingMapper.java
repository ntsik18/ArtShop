package com.ecommerce.ArtShop.Mapper;


import com.ecommerce.ArtShop.DTO.PaintingDTO.NewPaintingResponse;
import com.ecommerce.ArtShop.DTO.PaintingDTO.PaintingRequest;
import com.ecommerce.ArtShop.Model.Painting;
import com.ecommerce.ArtShop.Repository.ImageModelRepo;
import com.ecommerce.ArtShop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PaintingMapper {

    private final UserRepository userRepository;
    private final ImageModelRepo imageModelRepo;


    public Painting toPainting(PaintingRequest request) {
        return Painting.builder()
                .description(request.description())
                .price(request.price())
                .name(request.name())
                .quantity(request.quantity())
                .build();
    }


    public NewPaintingResponse toNewPaintingResponse(Painting painting) {
        return NewPaintingResponse.builder()
                .owner(painting.getUser().fullName())
                .description(painting.getDescription())
                .name(painting.getName())
                .price(painting.getPrice())
                .images(painting.getFinalImage().getName())
                .id(painting.getId())
                .quantity(painting.getQuantity())
                .build();
    }
}

