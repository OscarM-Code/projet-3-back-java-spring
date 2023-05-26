package com.projetjavaopc.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projetjavaopc.api.models.Users;

import com.projetjavaopc.api.tools.specialModel.BasicResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.response.ResponseProvider;
import com.projetjavaopc.api.repository.UserRepository;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "http://localhost:8080")
@Api(tags = "Users", description = "Endpoints for managing users")
public class UsersController {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    ResponseProvider responseProvider;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Get a user by ID", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User found", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<BasicResponse> createRental(@ApiParam(value = "ID of the user", required = true) @RequestParam Long id) 
    {
        try {
            Optional<Users> user = usersRepository.findById(id);
            return ResponseEntity.ok(responseProvider.response("User found", user));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error", e));
        }
    }
    
}
