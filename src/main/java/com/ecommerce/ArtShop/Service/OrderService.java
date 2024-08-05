package com.ecommerce.ArtShop.Service;


import com.ecommerce.ArtShop.Mapper.OrderMapper;
import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderRequest;
import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderResponse;
import com.ecommerce.ArtShop.Model.Order;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.OrderRepository;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper mapper;
    private final PaintingRepository paintingRepository;
    private final OrderRepository orderRepository;


    public OrderResponse placeOrder(OrderRequest request, Authentication connecteduser) {
        request.cardDetails().isValidExpirationDate();
        Double finalPrice = 0.0;
        User user = (User) connecteduser.getPrincipal();
        Map<Long, Integer> orders = request.paintingIdQuantity();
        for (Map.Entry<Long, Integer> entry : orders.entrySet()) {
            Double price = checkAvailableQuantity(entry.getKey(), entry.getValue());
            finalPrice = finalPrice + price;
        }
        Order order = mapper.toOrder(request, user);
        orderRepository.save(order);
        return OrderResponse.builder()
                .response(String.format("Thanks for ordering! Total value is %.2f", finalPrice))
                .build();
    }

    private Double checkAvailableQuantity(Long key, Integer value) {
        var painting = paintingRepository.findById(key);
        Integer quantity = painting.get().getQuantity();
        if (quantity == 0) {
            throw new RuntimeException(String.format("No painting with id %d is left in stock", key));
        }
        if (quantity < value) {
            throw new RuntimeException(String.format("Only %d paintings with id %d is left in stock", quantity, key));
        } else {
            painting.get().setQuantity(quantity - value);
            paintingRepository.save(painting.get());
            Double price = calculatePrice(key, value);
            return price;
        }
    }

    private Double calculatePrice(Long key, Integer value) {
        var painting = paintingRepository.findById(key);
        Double price = painting.get().getPrice() * value;
        return price;

    }
}
