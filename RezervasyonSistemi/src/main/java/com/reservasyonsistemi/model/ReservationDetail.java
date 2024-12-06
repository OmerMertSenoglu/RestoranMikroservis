package com.reservasyonsistemi.model;

public class ReservationDetail {
    private Reservation reservation;
    private RestaurantDTO restaurant;

    public ReservationDetail() {
    }
    public ReservationDetail(Reservation reservation, RestaurantDTO restaurant) {
        this.reservation = reservation;
        this.restaurant = restaurant;
    }
    public Reservation getReservation() {
        return reservation;
    }
    public void setReservation(Reservation reservation)
    {
        this.reservation = reservation;
    }
    public RestaurantDTO getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(RestaurantDTO restaurant)
    {
        this.restaurant = restaurant;
    }
}
