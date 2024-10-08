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
                                        .requestMatchers("/ws/**").permitAll()
                                        .requestMatchers("/api/admin/**").hasAuthority(ADMIN.name())
                                        .requestMatchers("/api/seller/**").hasAuthority(SELLER.name())
                                        .requestMatchers(HttpMethod.POST, "/api/product/category/add").hasAuthority(ADMIN.name())
                                        .requestMatchers(HttpMethod.POST, "api/product/update").hasAuthority(SELLER.name())
                                        .requestMatchers(HttpMethod.POST, "/api/product/save").hasAuthority(SELLER.name())

                                        .requestMatchers(HttpMethod.POST,"/api/store/create").hasAnyAuthority(SELLER.name(), USER.name())
                                        .requestMatchers("/api/store/**").hasAnyAuthority(SELLER.name(), ADMIN.name())
                                        .requestMatchers(HttpMethod.POST, "api/product/rate").hasAnyAuthority(USER.name(), SELLER.name())
                                        .requestMatchers(HttpMethod.DELETE,"/api/product/delete/**").hasAnyAuthority(SELLER.name(), ADMIN.name())
                                        .requestMatchers("/api/inventory/**").hasAnyAuthority(SELLER.name(), ADMIN.name())

                                        .requestMatchers(HttpMethod.GET, "/api/user").authenticated()
                                        .requestMatchers("/api/user/favorites/**").authenticated()
                                        .requestMatchers("/api/cart/**").authenticated()
                                        .requestMatchers("api/order/**").authenticated()

                                        .requestMatchers(HttpMethod.GET, "/api/follow/count/**").permitAll()
                                        .requestMatchers( "/api/rating/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                                        .requestMatchers("/api/user/image/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/rate/store/count/**").permitAll()
                                        .requestMatchers("/api/product/**").permitAll()

                                        .anyRequest().authenticated()
                );
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
