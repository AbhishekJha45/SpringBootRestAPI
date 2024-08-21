package com.example.AuthenticationService.service;

import com.example.AuthenticationService.model.User;
import com.example.AuthenticationService.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(email);
        builder.password(user.getPassword());
        builder.roles(user.getRole());
        return builder.build();
    }
}

