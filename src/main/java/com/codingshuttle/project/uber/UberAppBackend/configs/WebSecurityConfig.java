package com.codingshuttle.project.uber.UberAppBackend.configs;

import com.codingshuttle.project.uber.UberAppBackend.filter.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private static final String[] PUBLIC_ROUTES = {"/auth/**"};


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer ->httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(
                        AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(
                                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                                .requestMatchers(PUBLIC_ROUTES).permitAll()
                                                .requestMatchers("/public/**").authenticated()
                                        //or
                                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);



        return httpSecurity
                .build();
    }
}
