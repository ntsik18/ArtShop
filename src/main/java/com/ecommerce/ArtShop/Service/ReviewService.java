package com.ecommerce.ArtShop.Service;


import com.ecommerce.ArtShop.Mapper.ReviewMapper;
import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewRequest;
import com.ecommerce.ArtShop.DTO.ReviewDTO.ReviewResponse;
import com.ecommerce.ArtShop.Model.Review;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo repository;
    private final ReviewMapper reviewMapper;


    public ReviewResponse addReview(ReviewRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Review review = reviewMapper.toReview(request);
        review.setUser(user);
        repository.save(review);
        return reviewMapper.toResponse(review);
    }


}
