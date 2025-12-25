package com.attendance.service;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;

/**
 * Service to initialize sample data when the database is empty
 */
@Service
public class DataInitializationService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationService(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            SectionRepository sectionRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            AttendanceRepository attendanceRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.sectionRepository = sectionRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.attendanceRepository = attendanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeData() {
        if (userRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println("Initializing sample data...");

        // Create Admin User
        User adminUser = new User("admin", passwordEncoder.encode("admin123"), Role.ADMIN, "admin@university.edu");
        userRepository.save(adminUser);

        // Create Departments
        Department csDept = departmentRepository
                .save(new Department("Computer Science", "CS", "Computer Science and Engineering"));
        Department eceDept = departmentRepository
                .save(new Department("Electronics", "ECE", "Electronics and Communication"));

        // Create Courses
        Course bcaCourse = courseRepository.save(new Course(csDept, "BCA", "BCA", 3));
        Course mcaCourse = courseRepository.save(new Course(csDept, "MCA", "MCA", 2));

        // Create Subjects
        Subject java = subjectRepository.save(new Subject(bcaCourse, "Java Programming", "CS101", 3, 4));
        Subject python = subjectRepository.save(new Subject(bcaCourse, "Python Programming", "CS102", 3, 4));
        Subject webDev = subjectRepository.save(new Subject(bcaCourse, "Web Development", "CS103", 3, 3));
        Subject dbms = subjectRepository.save(new Subject(bcaCourse, "Database Systems", "CS104", 3, 4));

        // Create Sections
        Section sectionA = sectionRepository.save(new Section(bcaCourse, "BCA-A", 3, 2024));
        Section sectionB = sectionRepository.save(new Section(bcaCourse, "BCA-B", 3, 2024));

        // Create Teachers
        User teacherUser1 = userRepository.save(new User("john.smith", passwordEncoder.encode("teacher123"),
                Role.TEACHER, "john.smith@university.edu"));
        Teacher teacher1 = teacherRepository.save(new Teacher(teacherUser1, "John Smith", csDept));

        User teacherUser2 = userRepository.save(new User("sarah.jones", passwordEncoder.encode("teacher123"),
                Role.TEACHER, "sarah.jones@university.edu"));
        Teacher teacher2 = teacherRepository.save(new Teacher(teacherUser2, "Sarah Jones", csDept));

        // Assign Teachers to Subjects
        teacherSubjectRepository.save(new TeacherSubject(teacher1, java, sectionA));
        teacherSubjectRepository.save(new TeacherSubject(teacher1, python, sectionA));
        teacherSubjectRepository.save(new TeacherSubject(teacher2, webDev, sectionA));
        teacherSubjectRepository.save(new TeacherSubject(teacher2, dbms, sectionA));

        // Create Students
        String[] studentNames = { "Alice Johnson", "Bob Williams", "Charlie Brown", "Diana Davis", "Ethan Miller" };
        for (int i = 0; i < studentNames.length; i++) {
            String username = studentNames[i].toLowerCase().replace(" ", ".");
            User studentUser = userRepository.save(new User(username, passwordEncoder.encode("student123"),
                    Role.STUDENT, username + "@university.edu"));
            Student student = studentRepository.save(
                    new Student(studentUser, sectionA, "BCA2024" + String.format("%03d", i + 1), studentNames[i]));

            // Generate sample attendance for each student
            generateSampleAttendance(student, java, teacher1);
            generateSampleAttendance(student, python, teacher1);
        }

        System.out.println("Sample data initialized successfully!");
    }

    private void generateSampleAttendance(Student student, Subject subject, Teacher teacher) {
        Random random = new Random();
        LocalDate today = LocalDate.now();
        User markedBy = teacher.getUser();

        // Generate attendance for last 30 days
        for (int i = 30; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            // Skip weekends
            if (date.getDayOfWeek().getValue() > 5)
                continue;

            int chance = random.nextInt(100);
            AttendanceStatus status;
            if (chance < 80) {
                status = AttendanceStatus.PRESENT;
            } else if (chance < 95) {
                status = AttendanceStatus.ABSENT;
            } else {
                status = AttendanceStatus.LATE;
            }

            Attendance attendance = new Attendance(student, subject, date, 1, status, markedBy,
                    status == AttendanceStatus.LATE);
            attendanceRepository.save(attendance);
        }
    }
}
