package com.ecommerce.ArtShop.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "_Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private Double total;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @ManyToMany
    @JoinTable(
            name = "order_painting",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "painting_id")
    )
    List<Painting> paintings;


}
