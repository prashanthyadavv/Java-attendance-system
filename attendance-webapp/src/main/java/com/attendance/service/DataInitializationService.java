package com.attendance.service;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;

/**
 * Service to initialize sample data for Joginpally B.R. Engineering College
 * (JBREC)
 * Website: https://jbrec.edu.in
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

        System.out.println("Initializing JBREC data...");

        // Create Admin User
        User adminUser = new User("admin", passwordEncoder.encode("cybrito"), Role.ADMIN, "admin@jbrec.edu.in");
        userRepository.save(adminUser);

        // Create Department - Data Science
        Department dsDept = departmentRepository
                .save(new Department("Data Science", "DS", "Department of Data Science"));

        // Create Course - B.Tech Data Science (4 years)
        Course btechDS = courseRepository.save(new Course(dsDept, "B.Tech", "BTECH-DS", 4));

        // Create Subjects for Semester 4
        Subject javaProg = subjectRepository.save(new Subject(btechDS, "Java Programming", "DS401", 4, 4));
        Subject dataStructures = subjectRepository.save(new Subject(btechDS, "Data Structures", "DS402", 4, 4));
        Subject dataVisualization = subjectRepository.save(new Subject(btechDS, "Data Visualization", "DS403", 4, 3));

        // Create Section - A (Semester 4, 2nd Year, Academic Year 2025-2026)
        Section sectionA = sectionRepository.save(new Section(btechDS, "A", 4, 2025));

        // Create Teachers
        // Teacher 1 - Mrs Susmitha (Java Programming)
        User teacherUser1 = userRepository.save(new User("susmitha", passwordEncoder.encode("susmitha"),
                Role.TEACHER, "susmitha@jbrec.edu.in"));
        Teacher teacher1 = teacherRepository.save(new Teacher(teacherUser1, "Mrs Susmitha", dsDept));

        // Teacher 2 - Mrs Gayatri (Data Structures)
        User teacherUser2 = userRepository.save(new User("gayatri", passwordEncoder.encode("gayatri"),
                Role.TEACHER, "gayatri@jbrec.edu.in"));
        Teacher teacher2 = teacherRepository.save(new Teacher(teacherUser2, "Mrs Gayatri", dsDept));

        // Teacher 3 - Ms Satya Vaishnavi (Data Visualization)
        User teacherUser3 = userRepository.save(new User("vaishnavi", passwordEncoder.encode("vaishnavi"),
                Role.TEACHER, "vaishnavi@jbrec.edu.in"));
        Teacher teacher3 = teacherRepository.save(new Teacher(teacherUser3, "Ms Satya Vaishnavi", dsDept));

        // Assign Teachers to Subjects
        teacherSubjectRepository.save(new TeacherSubject(teacher1, javaProg, sectionA));
        teacherSubjectRepository.save(new TeacherSubject(teacher2, dataStructures, sectionA));
        teacherSubjectRepository.save(new TeacherSubject(teacher3, dataVisualization, sectionA));

        // Create Students
        // Student 1 - Prashanth Yadav Pittakala
        User studentUser1 = userRepository.save(new User("24j21a6741", passwordEncoder.encode("24j21a6741"),
                Role.STUDENT, "24j21a6741@jbrec.edu.in"));
        Student student1 = studentRepository
                .save(new Student(studentUser1, sectionA, "24J21A6741", "Prashanth Yadav Pittakala"));

        // Student 2 - B. Adi Keshav Reddy
        User studentUser2 = userRepository.save(new User("24j21a6708", passwordEncoder.encode("24j21a6708"),
                Role.STUDENT, "24j21a6708@jbrec.edu.in"));
        Student student2 = studentRepository
                .save(new Student(studentUser2, sectionA, "24J21A6708", "B. Adi Keshav Reddy"));

        // Student 3 - Jeshwanth Baikani
        User studentUser3 = userRepository.save(new User("24j21a6705", passwordEncoder.encode("24j21a6705"),
                Role.STUDENT, "24j21a6705@jbrec.edu.in"));
        Student student3 = studentRepository
                .save(new Student(studentUser3, sectionA, "24J21A6705", "Jeshwanth Baikani"));

        // Student 4 - Dhana Lakshmi Bandari
        User studentUser4 = userRepository.save(new User("24j21a6713", passwordEncoder.encode("24j21a6713"),
                Role.STUDENT, "24j21a6713@jbrec.edu.in"));
        Student student4 = studentRepository
                .save(new Student(studentUser4, sectionA, "24J21A6713", "Dhana Lakshmi Bandari"));

        // Student 5 - Srilatha Vemala
        User studentUser5 = userRepository.save(new User("24j21a6754", passwordEncoder.encode("24j21a6754"),
                Role.STUDENT, "24j21a6754@jbrec.edu.in"));
        Student student5 = studentRepository.save(new Student(studentUser5, sectionA, "24J21A6754", "Srilatha Vemala"));

        // Generate sample attendance for students
        Student[] students = { student1, student2, student3, student4, student5 };
        Subject[] subjects = { javaProg, dataStructures, dataVisualization };
        Teacher[] teachers = { teacher1, teacher2, teacher3 };

        for (int i = 0; i < subjects.length; i++) {
            for (Student student : students) {
                generateSampleAttendance(student, subjects[i], teachers[i]);
            }
        }

        System.out.println("JBREC data initialized successfully!");
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
