package com.techmarket.user_service;

import com.techmarket.user_service.model.User;
import com.techmarket.user_service.model.enums.Role;
import com.techmarket.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminEmail = "admin@shop.com";

			userRepository.findByEmail(adminEmail).ifPresentOrElse(
					user -> {},
					() -> {
						User admin = new User();
						admin.setEmail(adminEmail);
						admin.setPassword(passwordEncoder.encode("Admin123!")); // hashed password
						admin.setRole(Role.ADMIN);
						userRepository.save(admin);
						System.out.println("Admin created: " + adminEmail);
					}
			);
		};
	}
}
