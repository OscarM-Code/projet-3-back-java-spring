package com.projetjavaopc.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projetjavaopc.api.tools.specialModel.BasicResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.response.ResponseProvider;
import com.projetjavaopc.api.models.Rentals;
import com.projetjavaopc.api.repository.RentalsRepository;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/rentals")
@CrossOrigin(origins = "http://localhost:8080")
@Api(tags = "Rentals", description = "Endpoints for managing rentals")
public class RentalsController {

    @Autowired
    private RentalsRepository rentalsRepository;

    @Autowired
    ResponseProvider responseProvider;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Get all rentals", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rentals found", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @GetMapping("/")
    public ResponseEntity<BasicResponse> getAllrentals() {
        try {
            List<Rentals> rentals = rentalsRepository.findAll();
            return ResponseEntity.ok(responseProvider.response("Rentals founds", rentals));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error", e));
        }
    }

    @ApiOperation(value = "Create a rental", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental created successfully", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @PostMapping(value = "")
    public ResponseEntity<BasicResponse> createRental(@ApiParam(value = "Rental object to be created", required = true) @RequestBody Rentals rental) 
    {
        try {
            Rentals rentalSaved = rentalsRepository.save(rental);
            return ResponseEntity.ok(responseProvider.response("Rental created successfully", rentalSaved));
        } 
        catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error during rentals creation", e));
        }
    }

    @ApiOperation(value = "Get a rental by ID", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental found", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<BasicResponse> getRental(@ApiParam(value = "ID of the rental", required = true) @RequestParam Long id) 
    {
        try {
            Optional<Rentals> rental = rentalsRepository.findById(id);
            return ResponseEntity.ok(responseProvider.response("Rentals found", rental));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error", e));
        }
    }

    @ApiOperation(value = "Update a rental by ID", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental updated successfully", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<BasicResponse> updateRental(@ApiParam(value = "ID of the rental", required = true) @RequestParam Long id) 
    {
        try {
            Optional<Rentals> rental = rentalsRepository.findById(id);
            return ResponseEntity.ok(responseProvider.response("Rentals founds", rental));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error", e));
        }
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
      }
    
}
