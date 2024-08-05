package com.ecommerce.ArtShop.Controller;


import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewRequest;
import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewResponse;
import com.ecommerce.ArtShop.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;


    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> addreview(@RequestBody ReviewRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(reviewService.addReview(request, connectedUser));
    }
}
