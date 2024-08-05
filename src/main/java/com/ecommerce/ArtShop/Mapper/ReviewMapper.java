package com.ecommerce.ArtShop.Mapper;

import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewRequest;
import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewResponse;
import com.ecommerce.ArtShop.Model.Review;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewMapper {

    @Autowired
    private final PaintingRepository repository;

    public Review toReview(ReviewRequest request) {
        return Review.builder()
                .review(request.comment())
                .painting(repository.findById(request.paintingId()).get())
                .build();
    }

    public ReviewResponse toResponse(Review review) {

        return ReviewResponse.builder()
                .review(review.getReview())
                .id(review.getId())
                .painting_name(review.getPainting().getName())
                .painting_Id(review.getPainting().getId())
                .user_Id(review.getUser().getId())
                .build();

    }
}
