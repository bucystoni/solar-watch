package com.codecool.solarwatch.configuration;

import com.codecool.solarwatch.model.AppUser;
import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository,
                                       PasswordEncoder encoder,
                                       @Value("${app.admin.username}") String adminUsername,
                                       @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isPresent()) return;
            String hash = encoder.encode(adminPassword);
            AppUser admin = new AppUser(adminUsername, hash);
            admin.addRole(Role.USER);
            admin.addRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Seeded admin user");
        };
    }
}
