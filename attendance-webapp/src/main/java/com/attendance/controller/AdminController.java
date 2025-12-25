package com.attendance.controller;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            SectionRepository sectionRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.sectionRepository = sectionRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentRepository.count());
        model.addAttribute("totalTeachers", teacherRepository.count());
        model.addAttribute("totalDepartments", departmentRepository.count());
        model.addAttribute("totalCourses", courseRepository.count());
        model.addAttribute("totalSubjects", subjectRepository.count());
        model.addAttribute("totalSections", sectionRepository.count());
        return "admin/dashboard";
    }

    // Department Management
    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("department", new Department());
        return "admin/departments";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentRepository.save(department);
        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentRepository.deleteById(id);
        return "redirect:/admin/departments";
    }

    // Course Management
    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("course", new Course());
        return "admin/courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@RequestParam Long departmentId, @RequestParam String name,
            @RequestParam String code, @RequestParam int durationYears) {
        Department dept = departmentRepository.findById(departmentId).orElse(null);
        if (dept != null) {
            courseRepository.save(new Course(dept, name, code, durationYears));
        }
        return "redirect:/admin/courses";
    }

    // Subject Management
    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/subjects";
    }

    @PostMapping("/subjects/add")
    public String addSubject(@RequestParam Long courseId, @RequestParam String name,
            @RequestParam String code, @RequestParam int semester, @RequestParam int credits) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            subjectRepository.save(new Subject(course, name, code, semester, credits));
        }
        return "redirect:/admin/subjects";
    }

    // Section Management
    @GetMapping("/sections")
    public String sections(Model model) {
        model.addAttribute("sections", sectionRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/sections";
    }

    @PostMapping("/sections/add")
    public String addSection(@RequestParam Long courseId, @RequestParam String name,
            @RequestParam int semester, @RequestParam int year) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            sectionRepository.save(new Section(course, name, semester, year));
        }
        return "redirect:/admin/sections";
    }

    // User Management
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("sections", sectionRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String username, @RequestParam String email,
            @RequestParam String password, @RequestParam String name,
            @RequestParam String role, @RequestParam(required = false) Long sectionId) {

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(role));
        user.setActive(true);
        user = userRepository.save(user);

        // Create Teacher or Student based on role
        if ("TEACHER".equals(role)) {
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setName(name);
            teacherRepository.save(teacher);
        } else if ("STUDENT".equals(role) && sectionId != null) {
            Section section = sectionRepository.findById(sectionId).orElse(null);
            if (section != null) {
                Student student = new Student();
                student.setUser(user);
                student.setName(name);
                student.setRollNumber("ROLL" + user.getId());
                student.setSection(section);
                studentRepository.save(student);
            }
        }

        return "redirect:/admin/users";
    }

    // Teacher Assignment
    @GetMapping("/allotments")
    public String allotments(Model model) {
        model.addAttribute("allotments", teacherSubjectRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("sections", sectionRepository.findAll());
        return "admin/allotments";
    }

    @PostMapping("/allotments/add")
    public String addAllotment(@RequestParam Long teacherId, @RequestParam Long subjectId,
            @RequestParam Long sectionId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        Section section = sectionRepository.findById(sectionId).orElse(null);
        if (teacher != null && subject != null && section != null) {
            teacherSubjectRepository.save(new TeacherSubject(teacher, subject, section));
        }
        return "redirect:/admin/allotments";
    }
}
