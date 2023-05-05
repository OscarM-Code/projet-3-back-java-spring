package com.projetjavaopc.api.controller;

import java.util.Date;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.projetjavaopc.api.models.Users;
import com.projetjavaopc.api.dto.UserDto;
import com.projetjavaopc.api.tools.userService.UserService;
import com.projetjavaopc.api.tools.password.PasswordUtil;
import com.projetjavaopc.api.tools.specialModel.BasicResponse;
import com.projetjavaopc.api.tools.response.ResponseProvider;
import com.projetjavaopc.api.repository.UserRepository;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;



@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
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

    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(responseProvider.response("An error occured", null));
        }
        
        // Créer un objet User à partir de UserDto
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setCreatedAt(new Date());
    
        try {
            Users savedUser = userService.createUser(user);
            String token = userService.login(savedUser);
            return ResponseEntity.ok(responseProvider.response("user registered successfully", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(responseProvider.response(e.toString(), null));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<BasicResponse> login(@RequestBody Users user) 
    {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(responseProvider.response("Field email is empty", null));
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(responseProvider.response("Field password is empty", null));
        }

        String token = null;
        Users userToLog = userRepository.findByEmail(user.getEmail());
        String passwordToSend = passwordUtil.removeKeys(userToLog.getPassword());
        String passwordToCheck = passwordUtil.addKeys(user.getPassword());
        
        if (passwordUtil.checkPassword(passwordToSend, passwordToCheck))
        {
            userToLog.setPassword(passwordToCheck);
            token = userService.login(userToLog);
        }
        

        if(token == null) {
            return ResponseEntity.badRequest().body(responseProvider.response("Password incorrect", null));
        } else {
            return ResponseEntity.ok(responseProvider.response("User login successfully", token));
        }
        
    }

    @GetMapping(value = "/me")
    public ResponseEntity<BasicResponse> getUser(@RequestHeader("Authorization") String token) 
    {
        token = tokenProvider.extractBearer(token);
        String mail = "AAA";
        Users existingUser = userRepository.findByEmail(mail);
        if(existingUser != null) {
            existingUser.setPassword("");
            return ResponseEntity.ok(responseProvider.response(mail, existingUser));
        } else {
            return ResponseEntity.badRequest().body(responseProvider.response("User with this mail don't exist", null));
        }
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
      }
    
}
