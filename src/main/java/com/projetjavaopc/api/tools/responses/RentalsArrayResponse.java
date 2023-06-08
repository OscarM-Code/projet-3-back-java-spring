package com.projetjavaopc.api.tools.responses;

import java.util.List;

import com.projetjavaopc.api.models.Rentals;

public class RentalsArrayResponse {

    List<Rentals> rentals;

    public List<Rentals> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rentals> rentals) {
        this.rentals = rentals;
    }
    
}
