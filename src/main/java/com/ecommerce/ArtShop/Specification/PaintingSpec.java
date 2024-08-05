package com.ecommerce.ArtShop.Specification;

import com.ecommerce.ArtShop.Model.Painting;
import com.ecommerce.ArtShop.Model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PaintingSpec {
    public static Specification<Painting> containsText(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null || text.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Painting> containsUser(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + name.toLowerCase() + "%";
            Join<Painting, User> userJoin = root.join("user");
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")), likePattern)
            );
        };
    }
}
