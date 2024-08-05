package com.ecommerce.ArtShop.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "painting", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @ManyToMany(mappedBy = "paintings", cascade = CascadeType.REMOVE)
    private List<Order> orders;

    @OneToOne(mappedBy = "painting", cascade = CascadeType.REMOVE)
    private ImageModel finalImage;


}
