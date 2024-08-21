package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.util.jwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AuthenticationService.dto.AuthRequest;
import com.example.AuthenticationService.dto.AuthResponse;
import com.example.AuthenticationService.model.User;
import com.example.AuthenticationService.repository.userRepository;
import com.example.AuthenticationService.service.CustomUserDetailsService;
import com.example.AuthenticationService.service.TokenBlacklistService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private jwtToken jwtToken;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("User already exists.");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println(user.getPassword().toString());
        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtToken.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    @PostMapping("/refresh")
public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken.extractUsername(refreshToken));
        if (jwtToken.validateRefreshToken(refreshToken,userDetails)) {
            String newAccessToken = jwtToken.generateTokenFromRefreshToken(refreshToken,userDetails);
            return ResponseEntity.ok(new AuthResponse(newAccessToken));
        } else {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Something went wrong");
    }
}
@PostMapping("/revoke")
public ResponseEntity<?> revokeToken(@RequestBody String accessToken) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken.extractUsername(accessToken)); 
    if (jwtToken.validateToken(accessToken,userDetails)) {
        tokenBlacklistService.blacklistToken(accessToken);
        return ResponseEntity.ok("Token revoked successfully");
    } else {
        return ResponseEntity.status(401).body("Invalid token");
    }
}

}
