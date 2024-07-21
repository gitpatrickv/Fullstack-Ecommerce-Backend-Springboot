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
                                        .requestMatchers("/api/admin/**").hasAuthority(ADMIN.name())
                                        .requestMatchers("/api/seller/**").hasAuthority(SELLER.name())

                                        .requestMatchers("/api/store/**").hasAnyAuthority(SELLER.name(), ADMIN.name())
                                        .requestMatchers("/api/cart/**").hasAuthority(USER.name())

                                        .requestMatchers(HttpMethod.POST, "/api/product/category/add").hasAuthority(ADMIN.name())

                                        .requestMatchers(HttpMethod.POST, "/api/product/save").hasAuthority(SELLER.name())
                                        .requestMatchers(HttpMethod.DELETE,"/api/product/delete/**").hasAnyAuthority(SELLER.name(), ADMIN.name())
                                        .requestMatchers("/api/product/**").permitAll()

                                        .requestMatchers(HttpMethod.GET, "/api/user").authenticated()

                                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()

                                        .requestMatchers(HttpMethod.POST, "api/product/rate").hasAuthority(USER.name())
                                        .requestMatchers( "/api/rating/**").permitAll()


                                        .requestMatchers("/api/user/favorites/**").hasAuthority(USER.name())
                                        .requestMatchers("/api/user/image/**").permitAll()

                                        .requestMatchers("/api/inventory/**").hasAnyAuthority(SELLER.name(), ADMIN.name())

                                        .requestMatchers("api/order/**").hasAnyAuthority(SELLER.name(), USER.name())

                                        .anyRequest().authenticated()
                );
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
