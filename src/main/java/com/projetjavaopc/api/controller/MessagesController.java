package com.projetjavaopc.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projetjavaopc.api.tools.specialModel.BasicResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.response.ResponseProvider;
import com.projetjavaopc.api.models.Messages;
import com.projetjavaopc.api.repository.MessagesRepository;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "http://localhost:8080")
@Api(tags = "Messages", description = "Endpoints for managing messages")
public class MessagesController {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    ResponseProvider responseProvider;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Create a message", response = BasicResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Message created successfully", response = BasicResponse.class),
        @ApiResponse(code = 400, message = "Error during message creation", response = BasicResponse.class)
    })
    @PostMapping(value = "")
    public ResponseEntity<BasicResponse> createRental(@RequestHeader("Authorization") String token, @RequestBody Messages message) 
    {
        try {
            Messages messageSaved = messagesRepository.save(message);
            return ResponseEntity.ok(responseProvider.response("Message created successfully", messageSaved));
        } 
        catch (Exception e){
            return ResponseEntity.badRequest().body(responseProvider.response("Error during message creation", e));
        }
    }
    
}
