package com.ecommerce.ArtShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ArtShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtShopApplication.class, args);
    }

}
