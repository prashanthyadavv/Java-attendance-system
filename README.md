<p align="center">
  <h1 align="center">🎓 University Attendance Management System</h1>
  <p align="center">
    <strong>A modern attendance management system with both Desktop (Java Swing) and Web (Spring Boot) applications featuring a futuristic premium UI.</strong>
  </p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk" alt="Java 17+">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Swing-Desktop-blue?style=for-the-badge" alt="Swing GUI">
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="MIT License">
</p>

---

## 🚀 Two Deployment Options

| Version | Technology | Run Command |
|---------|------------|-------------|
| 🖥️ **Desktop App** | Java Swing | `run.bat` |
| 🌐 **Web App** | Spring Boot | `cd attendance-webapp && run-webapp.bat` |

---

## ✨ Features

### 🔐 Role-Based Access Control
| Role | Capabilities |
|------|-------------|
| **👔 Admin** | Full system control - manage departments, courses, subjects, sections, users & allotments |
| **👨‍🏫 Teacher** | Mark attendance, edit records, generate reports for assigned subjects |
| **🎒 Student** | View personal attendance dashboard, calendar, and statistics |

### 🎨 Premium Futuristic UI
- **Glassmorphism Design** - Blur effects with transparency
- **Neon Glow Accents** - Cyan & purple gradient highlights
- **Dark/Light Themes** - Toggle with ☀️/🌙 button
- **Smooth Animations** - Hover effects, transitions, micro-interactions
- **Responsive Layout** - Works on desktop, tablet, and mobile

---

## 🌐 Web Application (NEW!)

### Quick Start
```bash
cd attendance-webapp
run-webapp.bat
```
Then open: **http://localhost:8080**

### 🔑 Login Credentials
| Role | Username | Password |
|:----:|:--------:|:--------:|
| 👔 **Admin** | `admin` | `admin123` |
| 👨‍🏫 **Teacher** | `john.smith` | `teacher123` |
| 🎒 **Student** | `alice.johnson` | `student123` |

### 📦 Web App Tech Stack
- **Backend**: Spring Boot 3.2, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Custom CSS with CSS Variables
- **Database**: H2 (in-memory)
- **Build**: Maven

---

## 🖥️ Desktop Application

### Quick Start
```bash
# Windows
run.bat

# Or compile manually
javac -encoding UTF-8 -d bin -sourcepath src src/attendance/App.java
java -cp bin attendance.App
```

### 🎨 Desktop Themes
| Theme | Description |
|-------|-------------|
| 🌌 **Futuristic** | Neon accents with cyberpunk aesthetics |
| 💼 **Professional** | Clean, corporate-friendly design |
| 🎯 **User-Friendly** | Warm colors, high accessibility |

---

## 📁 Project Structure

```
📦 University Attendance System
├── 📂 attendance-webapp/         # 🌐 Spring Boot Web Application
│   ├── 📂 src/main/java/         # Java source code
│   │   └── com/attendance/
│   │       ├── controller/       # REST & MVC Controllers
│   │       ├── model/            # JPA Entities
│   │       ├── repository/       # Data Repositories
│   │       └── service/          # Business Logic
│   ├── 📂 src/main/resources/
│   │   ├── templates/            # Thymeleaf HTML templates
│   │   ├── static/css/           # Premium CSS with glassmorphism
│   │   └── static/js/            # Theme toggle script
│   ├── Dockerfile                # For cloud deployment
│   └── pom.xml                   # Maven dependencies
│
├── 📂 src/attendance/            # 🖥️ Java Swing Desktop App
│   ├── App.java                  # Entry point
│   ├── 📂 models/                # Data models
│   ├── 📂 database/              # DataStore
│   └── 📂 ui/                    # Swing UI components
│
├── run.bat                       # Run desktop app
└── README.md
```

---

## ☁️ Cloud Deployment (Render)

The web app can be deployed to Render.com:

1. Push to GitHub
2. Create new Web Service on Render
3. Select **Docker** as runtime
4. Set Root Directory: `attendance-webapp`
5. Deploy!

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| **Web Backend** | Spring Boot 3.2, Java 17 |
| **Web Frontend** | Thymeleaf, CSS3, JavaScript |
| **Desktop App** | Java Swing |
| **Database** | H2 (embedded) |
| **Authentication** | Spring Security |
| **Build Tool** | Maven |

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Prashanth Yadav**

---

<p align="center">
  <strong>⭐ Star this repository if you found it helpful! ⭐</strong>
</p>
