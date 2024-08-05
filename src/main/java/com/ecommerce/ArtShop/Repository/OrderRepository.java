package com.ecommerce.ArtShop.Repository;


import com.ecommerce.ArtShop.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
