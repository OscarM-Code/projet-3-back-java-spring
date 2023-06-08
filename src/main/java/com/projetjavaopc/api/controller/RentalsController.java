package com.projetjavaopc.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.responses.MessageResponse;
import com.projetjavaopc.api.tools.responses.RentalsArrayResponse;
import com.projetjavaopc.api.tools.services.RentalService;
import com.projetjavaopc.api.dto.RentalDto;
import com.projetjavaopc.api.error.CreateRentalException;
import com.projetjavaopc.api.error.ErrorResponse;
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
    private RentalService rentalService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Get all rentals", response = MessageResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rentals found", response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageResponse.class)
    })
    @GetMapping("/")
    public ResponseEntity<RentalsArrayResponse> getAllrentals(@ApiParam(value = "Bearer token for authentication", required = true) @RequestHeader("Authorization") String token) {
        try {
            List<Rentals> rentals = rentalsRepository.findAll();
            RentalsArrayResponse rentalsResponse = new RentalsArrayResponse();
            rentalsResponse.setRentals(rentals);
            return ResponseEntity.ok(rentalsResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ApiOperation(value = "Create a rental", response = MessageResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental created successfully", response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageResponse.class)
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRental(@RequestHeader("Authorization") String token, @ModelAttribute @ApiParam(value = "Rental object to be created", required = true) RentalDto rental) {
        
        MessageResponse message = new MessageResponse();
        try {
            token = tokenProvider.extractBearer(token);
            Claims claims = tokenProvider.extractClaims(token);
            Long id = claims.get("id", Long.class); 
            rentalService.createRentals(rental, id);
            message.setMessage("Rental created!");
            return ResponseEntity.ok(message);
        } catch (CreateRentalException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @ApiOperation(value = "Get a rental by ID", response = MessageResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental found", response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Rentals>> getRental(@ApiParam(value = "Bearer token for authentication", required = true) @RequestHeader("Authorization") String token, @ApiParam(value = "ID of the rental", required = true) @RequestParam Long id) 
    {
        try {
            Optional<Rentals> rental = rentalsRepository.findById(id);
            return ResponseEntity.ok(rental);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ApiOperation(value = "Update a rental by ID", response = MessageResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Rental updated successfully", response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = MessageResponse.class)
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> updateRental(@ApiParam(value = "Bearer token for authentication", required = true) @RequestHeader("Authorization") String token, @ApiParam(value = "ID of the rental", required = true) @RequestParam Long id, @ApiParam(value = "Rentals With updated values", required = true) @RequestBody Rentals rental) 
    {
        MessageResponse message = new MessageResponse();
        try {
            rentalService.updateById(id, rental);
            message.setMessage("Rental updated !");
            return ResponseEntity.ok(message);
        } catch (Exception e){
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
    }
    
}
