package com.practice.fullstackbackendspringboot.config;


import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {

            if(!userRepository.existsByEmail("admin@gmail.com")) {
                User user = new User();
                user.setName("ADMIN");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRole(Role.ADMIN);
                userRepository.save(user);
                log.info(StringUtils.ADMIN_CREATED);
            }
            else{
                log.info(StringUtils.ADMIN_ALREADY_EXISTS);
            }
        };
    }
}

