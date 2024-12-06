package com.reservasyonsistemi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reservasyonsistemi.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
