package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * University Attendance Management System
 * Spring Boot Web Application
 * 
 * @author Prashanth Yadav
 */
@SpringBootApplication
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Attendance Management System Started");
        System.out.println("  Access at: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
