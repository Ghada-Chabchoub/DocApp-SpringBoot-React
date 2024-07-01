package com.example.pfabackendfinal.config;

import com.example.pfabackendfinal.entity.User;
import com.example.pfabackendfinal.repository.UserRepository;
import com.example.pfabackendfinal.services.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableMethodSecurity

public class SecurityConfig {
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
//pub httpSect fel par-> http

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf( cerf -> cerf.disable() );

          // http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/doctors/**").permitAll()
                        .requestMatchers("/bookings/**").permitAll()
                        .requestMatchers("/bookings/doctor/{doctorEmail}/booking").permitAll()
                        .requestMatchers("/bookings/{bookingId}").permitAll()
                        .requestMatchers("/bookings/**").permitAll()
                        .requestMatchers("/bookings/update/{bookingId}").permitAll()
                         .requestMatchers("/bookings/user/{email}/bookings").permitAll()
                        .requestMatchers("/doctors/add/new-doctor").permitAll()

                        .anyRequest().authenticated()

        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();



    }

}
