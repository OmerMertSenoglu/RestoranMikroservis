package com.reservasyonsistemi.service;

import com.reservasyonsistemi.feignclient.RestaurantFeignClient;
import com.reservasyonsistemi.model.Reservation;
import com.reservasyonsistemi.model.ReservationDetail;
import com.reservasyonsistemi.model.RestaurantDTO;
import com.reservasyonsistemi.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantFeignClient restaurantFeignClient;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RestaurantFeignClient restaurantFeignClient) {
        this.reservationRepository = reservationRepository;
        this.restaurantFeignClient = restaurantFeignClient;
    }
    public Reservation createReservation(Reservation reservation) {
        // Restoranın var olup olmadığını kontrol et
        RestaurantDTO restaurant = restaurantFeignClient.getRestaurantById(reservation.getRestaurantId());
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found with ID: " + reservation.getRestaurantId());
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<ReservationDetail> getReservationById(Long id) {
        var reservation = reservationRepository.findById(id);
        RestaurantDTO restaurant = restaurantFeignClient.getRestaurantById(reservation.get().getRestaurantId());
        return Optional.of(new ReservationDetail(reservation.get(), restaurant));
    }

    public Optional<Reservation> updateReservation(Long id, Reservation updatedReservation) {
        return reservationRepository.findById(id).map(reservation -> {
            reservation.setUsername(updatedReservation.getUsername());
            reservation.setReservationDate(updatedReservation.getReservationDate());
            reservation.setNumberOfPeople(updatedReservation.getNumberOfPeople());
            reservation.setRestaurantId(updatedReservation.getRestaurantId());
            reservation.setNotes(updatedReservation.getNotes());
            return reservationRepository.save(reservation);
        });
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
