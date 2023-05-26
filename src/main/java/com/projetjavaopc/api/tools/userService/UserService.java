package com.projetjavaopc.api.tools.userService;

import com.projetjavaopc.api.dto.UserDto;
import com.projetjavaopc.api.models.Users;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetjavaopc.api.repository.UserRepository;

import java.util.Date;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    /**
     *
     * @param username
     * @param password
     * @return
     */
    public String login(Users user) {
        log.info(user.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return jwtTokenProvider.createToken(user);
    }

    /**
     *
     * @param remoteUser
     * @return
     */
    public String refreshToken(Users user) {
        return jwtTokenProvider.createToken(user);
    }

    @Transactional(transactionManager = "transactionManager")
    public Users createUser(UserDto userDto) {
        // Créer un objet User à partir de UserDto
        Users user = new Users();
        user.setId(1234L);
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setCreatedAt(new Date());
        Users existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Un utilisateur avec cet e-mail existe déjà");
        }
        user.setCreatedAt(new Date());
        Users savedUser = userRepository.save(user);
        return savedUser;
    }

}