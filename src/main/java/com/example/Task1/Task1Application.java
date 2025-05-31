package com.example.Task1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Task1Application {

	public static void main(String[] args) {
		SpringApplication.run(Task1Application.class, args);
		System.out.println("Employee management system");
//		String rawPassword = "admin123";
//			String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
//		System.out.println("Encoded password: " + encodedPassword);
		}


	}
