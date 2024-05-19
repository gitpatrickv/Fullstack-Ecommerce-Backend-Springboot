package com.practice.fullstackbackendspringboot.config;

import com.practice.fullstackbackendspringboot.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.practice.fullstackbackendspringboot.entity.constants.Role.*;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                                authorize
                                        .requestMatchers(HttpMethod.POST, "/api/product/save").hasAnyAuthority(ADMIN.name(), SELLER.name())
                                        .requestMatchers("/api/store/**").hasAnyAuthority(ADMIN.name(), SELLER.name())
                                        .requestMatchers(HttpMethod.DELETE,"/api/product/delete/**").hasAuthority(SELLER.name())
                                        .requestMatchers("/api/cart/**").hasAuthority(USER.name())
                                        .requestMatchers("/api/product/**").permitAll()
                                        .requestMatchers("/api/user/**").permitAll()
                                        .anyRequest().authenticated()
                );
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
