# University Attendance Management System

A modern Java Swing desktop application for managing university attendance with role-based access control.

## Quick Start

### Option 1: Double-click to Run
```
run.bat
```

### Option 2: Command Line
```bash
cd "JAVA attendence management app"
javac -encoding UTF-8 -d bin -sourcepath src src/attendance/App.java src/attendance/**/*.java
java -cp bin attendance.App
```

## Deployment Options

### 1. Run from Source (Development)
```bash
# Compile
javac -encoding UTF-8 -d bin -sourcepath src src/attendance/App.java src/attendance/**/*.java

# Run
java -cp bin attendance.App
```

### 2. Create Deployable JAR
```bash
# Run the build script
build.bat

# Or manually:
jar cfm dist/AttendanceSystem.jar MANIFEST.MF -C bin .

# Run anywhere with Java 11+:
java -jar AttendanceSystem.jar
```

### 3. Deploy to Another Computer
1. Copy the entire project folder OR just `dist/AttendanceSystem.jar`
2. Ensure Java 11+ is installed on target machine
3. Run: `java -jar AttendanceSystem.jar`

### 4. Deploy to GitHub
```bash
git init
git add .
git commit -m "Attendance Management System"
git remote add origin https://github.com/YOUR_USERNAME/attendance-system.git
git push -u origin main
```

## Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher | john.smith | teacher123 |
| Teacher | sarah.jones | teacher123 |
| Student | alice.johnson | student123 |

## Features

- **Admin Dashboard**: Manage departments, courses, subjects, sections, users, allotments
- **Teacher Dashboard**: Mark attendance, edit records, view reports
- **Student Dashboard**: View personal attendance
- **Data Persistence**: Auto-saves to `data/attendance_data.dat`
- **Theme System**: 3 themes × 2 modes (dark/light)

## Project Structure

```
├── src/attendance/
│   ├── App.java              # Main entry point
│   ├── models/               # Data models
│   ├── database/             # DataStore & Persistence
│   ├── services/             # Authentication
│   └── ui/                   # Swing UI
├── bin/                      # Compiled classes
├── data/                     # Saved data
├── dist/                     # Deployable JAR
├── run.bat                   # Quick run script
├── build.bat                 # Create JAR script
└── MANIFEST.MF               # JAR manifest
```

## Requirements

- Java 11 or higher
- Windows/macOS/Linux
