# Attendance Management System - Web Application

A modern Spring Boot web application for managing university attendance with role-based access control.

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**

### Run the Application

```bash
cd attendance-webapp
mvn spring-boot:run
```

Or double-click `run-webapp.bat` on Windows.

Access at: **http://localhost:8080**

## 🔑 Demo Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Teacher | `john.smith` | `teacher123` |
| Teacher | `sarah.jones` | `teacher123` |
| Student | `alice.johnson` | `student123` |

## ✨ Features

- **Admin Dashboard**: Manage departments, courses, subjects, sections, users, teacher assignments
- **Teacher Dashboard**: Mark attendance, view assigned classes, generate reports
- **Student Dashboard**: View attendance statistics, calendar view, subject-wise breakdown

## 🎨 Tech Stack

- Spring Boot 3.2
- Thymeleaf Templates
- Spring Security
- Spring Data JPA
- H2 Database (embedded)
- Futuristic Dark Theme UI
