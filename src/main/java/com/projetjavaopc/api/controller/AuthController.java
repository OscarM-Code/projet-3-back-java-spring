package com.projetjavaopc.api.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.projetjavaopc.api.models.Users;
import com.projetjavaopc.api.dto.UserDto;
import com.projetjavaopc.api.tools.userService.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.password.PasswordUtil;
import com.projetjavaopc.api.tools.specialModel.BasicResponse;
import com.projetjavaopc.api.tools.response.ResponseProvider;
import com.projetjavaopc.api.repository.UserRepository;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
@Api(tags = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ResponseProvider responseProvider;

    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Register a new user", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User registered successfully", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@ApiParam(value = "User information for a new User to be created.", required = true) @Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            Users savedUser = userService.createUser(userDto);
            savedUser.setPassword(passwordUtil.addKeys(userDto.getPassword()));
            String token = userService.login(savedUser);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Login a user", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User logged in successfully", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@ApiParam(value = "User email and password are required for logging in.", required = true) @RequestBody Users user) 
    {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Field email is empty");
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Field password is empty");
        }

        String token = null;
        Users userToLog = userRepository.findByEmail(user.getEmail());
        String passwordToSend = passwordUtil.removeKeys(userToLog.getPassword());
        String passwordToCheck = passwordUtil.addKeys(user.getPassword());
        
        try {
            passwordUtil.checkPassword(passwordToSend, passwordToCheck);
            userToLog.setPassword(passwordToCheck);
            token = userService.login(userToLog);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @ApiOperation(value = "Get the current user", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User found", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Bad request", response = BasicResponse.class)
    })
    @GetMapping(value = "/me")
    public ResponseEntity<Users> getUser(@ApiParam(value = "Bearer token for authentication", required = true) @RequestHeader("Authorization") String token) 
    {
        token = tokenProvider.extractBearer(token);
        String mail = "AAA";
        Users existingUser = userRepository.findByEmail(mail);
        if(existingUser != null) {
            existingUser.setPassword("");
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}
