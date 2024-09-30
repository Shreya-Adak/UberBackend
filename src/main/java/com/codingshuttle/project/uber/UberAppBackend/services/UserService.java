package com.codingshuttle.project.uber.UberAppBackend.services;

import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(()->new ResourceNotFoundExceptions("EMAIL ID NOT FOUND: "+username));
    }

    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new ResourceNotFoundExceptions("USER NOT FOUNT WITH ID: "+userId));
    }
}
