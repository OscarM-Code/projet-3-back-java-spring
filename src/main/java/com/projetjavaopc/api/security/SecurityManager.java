package com.projetjavaopc.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.projetjavaopc.api.tools.jwt.JwtTokenFilterConfigurer;
import com.projetjavaopc.api.tools.jwt.JwtTokenProvider;

import java.util.List;


/**
 * Main configuration object for Spring Security (could be written using xml instead, but will be even more painful)
 */
@Configuration
@EnableWebSecurity
public class SecurityManager extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    /**
     * Configure the HTTP stack of Spring Security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Enable CORS support
                .cors().and()

                // Disable session for REST services
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Define global authorization
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/register").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
                .and()

                // Disable built-in mechanisms for authentication
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        // Setup JWT filter to extract and validate token on HTTP requests
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }


    /**
     * Configure the default password encoder
     *
     * @return A BCrypt password encoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Reimplement in order to provide a global AuthenticationManager throughout the app
     *
     * @return An AuthenticationManager instance
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * Configure the CORS filter to allow anything from everywhere
     */
    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}

