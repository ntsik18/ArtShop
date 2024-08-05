package com.ecommerce.ArtShop.Repository;

import com.ecommerce.ArtShop.Model.Address;
import com.ecommerce.ArtShop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {


    boolean existsByAddressLineAndCityAndCountryAndUser(String addressLine, String city, String country, User user);

}
