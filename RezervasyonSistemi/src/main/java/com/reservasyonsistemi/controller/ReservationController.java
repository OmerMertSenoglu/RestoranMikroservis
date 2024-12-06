package com.reservasyonsistemi.controller;

import com.reservasyonsistemi.model.ReservationDetail;
import com.reservasyonsistemi.repository.ReservationRepository;
import com.reservasyonsistemi.feignclient.RestaurantFeignClient;
import com.reservasyonsistemi.model.Reservation;
import com.reservasyonsistemi.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Reservation")
public class ReservationController {

    private final ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Autowired
    private RestaurantFeignClient restaurantFeignClient;

    // Create Reservation
    @PostMapping("/create")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        // Restoran ID doğrulama
        var restaurant = restaurantFeignClient.getRestaurantById(reservation.getRestaurantId());
        if (restaurant == null) {
            throw new IllegalArgumentException("Geçersiz restoran ID: " + reservation.getRestaurantId());
        }

        // Rezervasyonu kaydet
        return reservationService.createReservation(reservation);
    }

    // Get All Reservations
    @GetMapping("/findAll")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detayını konsola yazdır
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Reservation By ID
    @GetMapping("/get/{id}")
    public ResponseEntity<ReservationDetail> getReservationById(@PathVariable Long id) {
        Optional<ReservationDetail> reservation = reservationService.getReservationById(id);

        return reservation.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update Reservation
    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation reservation, @PathVariable Long id) {
        try {
            Optional<Reservation> updatedReservation = reservationService.updateReservation(id, reservation);
            return updatedReservation.map(res -> new ResponseEntity<>(res, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace(); // Hata detayını konsola yazdır
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Reservation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            if (reservationService.deleteReservation(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Hata detayını konsola yazdır
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
