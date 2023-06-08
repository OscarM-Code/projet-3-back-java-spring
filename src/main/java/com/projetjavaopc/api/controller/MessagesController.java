package com.projetjavaopc.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.projetjavaopc.api.tools.responses.MessageResponse;
import com.projetjavaopc.api.tools.services.MessageService;
import com.projetjavaopc.api.dto.MessageDto;
import com.projetjavaopc.api.error.CreateMessageException;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/messages")
@CrossOrigin(origins = "http://localhost:8080")
@Api(tags = "Messages", description = "Endpoints for managing messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Create a message", response = MessageResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Message created successfully", response = MessageResponse.class),
        @ApiResponse(code = 400, message = "Error during message creation", response = MessageResponse.class)
    })
    @PostMapping(value = "")
    public ResponseEntity<MessageResponse> createRental(@RequestHeader("Authorization") String token, @RequestBody MessageDto message) 
    {
        MessageResponse messageResponse = new MessageResponse();
        try {
            token = tokenProvider.extractBearer(token);
            Claims claims = tokenProvider.extractClaims(token);
            Long id = claims.get("id", Long.class);
            messageService.createMessage(message, id);
            messageResponse.setMessage("Message send with success");
            return ResponseEntity.ok(messageResponse);
        } 
        catch (CreateMessageException e){
            messageResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }
    
}
