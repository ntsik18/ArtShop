package com.ecommerce.ArtShop.Controller;


import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderRequest;
import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderResponse;
import com.ecommerce.ArtShop.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.placeOrder(request, connectedUser));
    }
}

