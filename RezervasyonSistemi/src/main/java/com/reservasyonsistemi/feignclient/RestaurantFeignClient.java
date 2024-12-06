package com.reservasyonsistemi.feignclient;

import com.reservasyonsistemi.model.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service", url = "http://localhost:8080/Restaurant/")
public interface RestaurantFeignClient {

    @GetMapping("/get/{id}")
    RestaurantDTO getRestaurantById(@PathVariable Long id);
}

