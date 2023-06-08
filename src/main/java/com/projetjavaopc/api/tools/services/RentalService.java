package com.projetjavaopc.api.tools.services;

import java.io.File;
import java.util.Date;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projetjavaopc.api.dto.RentalDto;
import com.projetjavaopc.api.error.CreateRentalException;
import com.projetjavaopc.api.models.Rentals;
import com.projetjavaopc.api.repository.RentalsRepository;

@Service
public class RentalService {

    @Autowired
    RentalsRepository rentalsRepository;

    public Rentals createRentals(RentalDto rental, Long id) {
        try {

            MultipartFile picture = rental.getPicture();
            String filename = picture.getOriginalFilename();
            String imageStorageDirectory = System.getProperty("user.dir") + File.separator + "assets/rentalsPictures";
            String filePath = imageStorageDirectory + File.separator + id + filename;
            File destFile = new File(filePath);
            picture.transferTo(destFile);

            Rentals rentalsToCreate = new Rentals();
            rentalsToCreate.setDescription(rental.getDescription());
            rentalsToCreate.setName(rental.getName());
            rentalsToCreate.setPrice(rental.getPrice());
            rentalsToCreate.setSurface(rental.getSurface());
            rentalsToCreate.setCreatedAt(new Date());
            rentalsToCreate.setPicture(filePath);
            rentalsToCreate.setOwnerId(id);
            rentalsRepository.save(rentalsToCreate);
            return rentalsToCreate;
        } catch(Exception e) {
            throw new CreateRentalException("Error creating rental: " + e.getMessage());
        }
    }

    public Rentals updateById(Long id, Rentals rental) {
        try {
            Rentals existingRental = rentalsRepository.findById(id).orElse(null);
            if (existingRental == null) {
                throw new CreateRentalException("Error creating rental: Rental not found");
            }
            
            Field[] fields = Rentals.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object updatedValue = field.get(rental);
                if (updatedValue != null) {
                    field.set(existingRental, updatedValue);
                }
            }
            
            existingRental.setUpdatedAt(new Date());
            rentalsRepository.save(existingRental);
            return existingRental;
        } catch(Exception e) {
            throw new CreateRentalException("Error creating rental: " + e.getMessage());
        }
    }

}
