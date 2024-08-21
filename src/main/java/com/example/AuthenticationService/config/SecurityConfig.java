package com.example.AuthenticationService.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.AuthenticationService.filter.JwtTokenFilter;
import com.example.AuthenticationService.service.CustomUserDetailsService;
import com.example.AuthenticationService.util.jwtToken;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired 
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenFilter jwtFilter;

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    // }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth->auth.anyRequest().permitAll()
                ).headers(headers -> headers.frameOptions(FrameOptionsConfig::disable)).sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
    }
//requestMatchers("/login","/api/auth/signup", "/api/auth/signin")
//.anyRequest().authenticated()
    @Bean
public AuthenticationManager authenticationManager(
        AuthenticationConfiguration auth) throws Exception {
    return auth.getAuthenticationManager();
}
}