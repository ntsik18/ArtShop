package com.ecommerce.ArtShop.Mapper;

import com.ecommerce.ArtShop.DTO.OrderDTUO.OrderRequest;
import com.ecommerce.ArtShop.Model.*;
import com.ecommerce.ArtShop.Repository.AddressRepository;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final PaintingRepository paintingRepository;
    private final AddressRepository addressRepository;

    public Order toOrder(OrderRequest request, User connectedUser) {
        Integer quantity = 0;
        Double total = 0.0;
        List<Painting> paintings = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : request.paintingIdQuantity().entrySet()) {
            var painting = paintingRepository.findById(entry.getKey());
            paintings.add(painting.get());
            quantity += entry.getValue();
            total = +painting.get().getPrice();
        }
        Address address = Address.builder()
                .user(connectedUser)
                .addressLine(request.addressLine())
                .city(request.city())
                .country(request.country())
                .build();
        boolean check = addressRepository.existsByAddressLineAndCityAndCountryAndUser(address.getAddressLine(), address.getCity(), address.getCountry(), address.getUser());
        if (!check) {
            addressRepository.save(address);
        }
        return Order.builder()
                .user(connectedUser)
                .total(total)
                .quantity(quantity)
                .paymentOption(PaymentOption.valueOf(request.paymentOption()))
                .address(address)
                .paintings(paintings)
                .build();
    }


}
