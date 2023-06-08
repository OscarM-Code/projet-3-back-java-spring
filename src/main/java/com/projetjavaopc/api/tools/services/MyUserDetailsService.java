package com.projetjavaopc.api.tools.services;

import com.projetjavaopc.api.models.Users;
import com.projetjavaopc.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projetjavaopc.api.tools.password.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * Security service responsible to retrieve user details from their email
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordUtil passwordUtil;

    @Value("${site.passkey.first}")
    String passkeyone;

    @Value("${site.passkey.second}")
    String passkeytwo;
    /**
     * Load a user from DB using its email address, and create a Spring UserDetails with the retrieved information
     *
     * @param email A user email
     * @return An instance of Spring UserDetails
     * @throws UsernameNotFoundException when no users were found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users u = userRepository.findByEmail(email);
        u.setPassword(passwordUtil.removeKeys(u.getPassword()));
        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(u.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

}

