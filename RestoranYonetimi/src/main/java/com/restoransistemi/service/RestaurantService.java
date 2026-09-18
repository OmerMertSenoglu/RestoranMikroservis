package com.restoransistemi.service;

import com.restoransistemi.model.Restaurant;
import com.restoransistemi.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Optional<Restaurant> updateRestaurant(Long id, Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id).map(restaurant -> {
            restaurant.setName(updatedRestaurant.getName());
            restaurant.setAddress(updatedRestaurant.getAddress());
            restaurant.setDescription(updatedRestaurant.getDescription());
            restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
            restaurant.setContactNumber(updatedRestaurant.getContactNumber());
            restaurant.setEmail(updatedRestaurant.getEmail());
            return restaurantRepository.save(restaurant);
        });
    }

    public boolean deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            return false;
        }

        restaurantRepository.deleteById(id);
        return true;
    }
}
