<p align="center">
  <h1 align="center">🎓 University Attendance Management System</h1>
  <p align="center">
    <strong>A modern, feature-rich Java Swing desktop application for managing university attendance with role-based access control and a stunning futuristic UI.</strong>
  </p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=java" alt="Java 11+">
  <img src="https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge" alt="Swing GUI">
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="MIT License">
  <img src="https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=for-the-badge" alt="Cross Platform">
</p>

---

## ✨ Features

### 🔐 Role-Based Access Control
| Role | Capabilities |
|------|-------------|
| **👨‍💼 Admin** | Full system control - manage departments, courses, subjects, sections, users & allotments |
| **👨‍🏫 Teacher** | Mark attendance, edit records, generate reports for assigned subjects |
| **👨‍🎓 Student** | View personal attendance dashboard and statistics |

### 🎨 Modern UI/UX
- **3 Beautiful Themes**: Futuristic, Professional, User-Friendly
- **2 Color Modes**: Dark Mode & Light Mode
- **Custom Components**: Glowing buttons, gradient panels, circular progress bars
- **Smooth Animations**: Visual feedback and transitions

### 📊 Comprehensive Features
- ✅ **Attendance Tracking** - Mark, edit, and manage daily attendance
- 📈 **Visual Reports** - Charts and statistics for attendance analysis
- 📤 **Export Options** - Export reports to CSV/PDF formats
- ⚠️ **Detention System** - Configurable attendance thresholds & alerts
- 💾 **Auto-Save** - Data persistence with automatic saving

---

## 🚀 Quick Start

### Prerequisites
- **Java 11** or higher installed on your system
- Verify with: `java -version`

### Option 1: Double-click to Run
Simply double-click `run.bat` (Windows) or run `./run.sh` (Mac/Linux)

### Option 2: Command Line
```bash
# Navigate to project directory
cd "JAVA attendence management app"

# Compile
javac -encoding UTF-8 -d bin -sourcepath src src/attendance/App.java src/attendance/**/*.java

# Run
java -cp bin attendance.App
```

---

## 🔑 Demo Credentials

| Role | Username | Password |
|:----:|:--------:|:--------:|
| 👨‍💼 **Admin** | `admin` | `admin123` |
| 👨‍🏫 **Teacher** | `john.smith` | `teacher123` |
| 👨‍🏫 **Teacher** | `sarah.jones` | `teacher123` |
| 👨‍🎓 **Student** | `alice.johnson` | `student123` |

---

## 📁 Project Structure

```
📦 JAVA Attendance Management App
├── 📂 src/attendance/           # Source code
│   ├── 📄 App.java              # Main entry point
│   ├── 📂 models/               # Data models (13 classes)
│   │   ├── User.java            # Base user class
│   │   ├── Student.java         # Student entity
│   │   ├── Teacher.java         # Teacher entity
│   │   ├── Department.java      # Department entity
│   │   ├── Course.java          # Course entity
│   │   ├── Subject.java         # Subject entity
│   │   ├── Section.java         # Section entity
│   │   ├── Attendance.java      # Attendance record
│   │   └── ...more
│   ├── 📂 database/             # Data layer
│   │   └── DataStore.java       # Singleton data manager
│   ├── 📂 services/             # Business logic
│   │   └── AuthService.java     # Authentication service
│   └── 📂 ui/                   # User interface
│       ├── 📂 frames/           # Main windows
│       │   ├── LoginFrame.java
│       │   ├── AdminDashboard.java
│       │   ├── TeacherDashboard.java
│       │   └── StudentDashboard.java
│       ├── 📂 components/       # Reusable UI components
│       │   ├── GlowButton.java
│       │   ├── GradientPanel.java
│       │   ├── CircularProgressBar.java
│       │   ├── ChartPanel.java
│       │   └── ...more
│       └── 📂 theme/            # Theme system
│           ├── ThemeColors.java
│           ├── ThemeManager.java
│           ├── FuturisticTheme.java
│           ├── ProfessionalTheme.java
│           └── ...more
├── 📂 bin/                      # Compiled classes
├── 📂 data/                     # Persisted data
├── 📂 dist/                     # Deployable JAR
├── 📂 reports/                  # Generated reports
├── 📄 run.bat                   # Quick run script
├── 📄 build.bat                 # Build JAR script
└── 📄 MANIFEST.MF               # JAR manifest
```

---

## 🏗️ Building & Deployment

### Create Deployable JAR
```bash
# Run the build script
build.bat

# Or manually create JAR
jar cfm dist/AttendanceSystem.jar MANIFEST.MF -C bin .

# Run the JAR anywhere with Java 11+
java -jar dist/AttendanceSystem.jar
```

### Deploy to Another Computer
1. Copy `dist/AttendanceSystem.jar` to target machine
2. Ensure Java 11+ is installed
3. Run: `java -jar AttendanceSystem.jar`

---

## 🎨 Themes & Customization

The application features a powerful theme system with **6 combinations**:

| Theme | Description |
|-------|-------------|
| 🌌 **Futuristic** | Neon accents with cyberpunk aesthetics |
| 💼 **Professional** | Clean, corporate-friendly design |
| 🎯 **User-Friendly** | Warm colors, high accessibility |

Each theme supports both **Dark Mode** 🌙 and **Light Mode** ☀️

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| **Java 11+** | Core programming language |
| **Swing** | Desktop GUI framework |
| **Custom Components** | GlowButton, GradientPanel, etc. |
| **Serialization** | Data persistence |

---

## 📈 Application Screenshots

### Admin Dashboard
- Manage all system entities
- View system-wide statistics
- Configure attendance rules

### Teacher Dashboard
- Mark student attendance
- Edit attendance records
- Generate class reports

### Student Dashboard
- View personal attendance
- Track attendance percentage
- Monitor detention status

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

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
