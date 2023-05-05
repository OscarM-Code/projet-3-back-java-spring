package com.projetjavaopc.api.tools.userService;

import com.projetjavaopc.api.models.Users;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.projetjavaopc.api.repository.UserRepository;

import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;



@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountValidationService accountValidationService;

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

    public Users createUser(Users user) {
        Users existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Un utilisateur avec cet e-mail existe déjà");
        }
        user.setCreatedAt(new Date());
        Users savedUser = userRepository.save(user);
        return savedUser;
    }

}