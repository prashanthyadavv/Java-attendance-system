package com.attendance.controller;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

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
    private final AttendanceRepository attendanceRepository;
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
    public String addDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentRepository.save(department);
            redirectAttributes.addFlashAttribute("success", "Department added successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Department code already exists!");
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete: Department has courses linked to it!");
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/edit/{id}")
    public String editDepartment(@PathVariable Long id, @RequestParam String name,
            @RequestParam String code, @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            Department dept = departmentRepository.findById(id).orElse(null);
            if (dept != null) {
                dept.setName(name);
                dept.setCode(code);
                dept.setDescription(description);
                departmentRepository.save(dept);
                redirectAttributes.addFlashAttribute("success", "Department updated successfully!");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Department code already exists!");
        }
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
            @RequestParam String code, @RequestParam int durationYears, RedirectAttributes redirectAttributes) {
        Department dept = departmentRepository.findById(departmentId).orElse(null);
        if (dept != null) {
            try {
                courseRepository.save(new Course(dept, name, code, durationYears));
                redirectAttributes.addFlashAttribute("success", "Course added successfully!");
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("error", "Course code already exists!");
            }
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete: Course has sections or subjects linked to it!");
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id, @RequestParam Long departmentId,
            @RequestParam String name, @RequestParam String code, @RequestParam int durationYears,
            RedirectAttributes redirectAttributes) {
        try {
            Course course = courseRepository.findById(id).orElse(null);
            Department dept = departmentRepository.findById(departmentId).orElse(null);
            if (course != null && dept != null) {
                course.setDepartment(dept);
                course.setName(name);
                course.setCode(code);
                course.setDurationYears(durationYears);
                courseRepository.save(course);
                redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Course code already exists!");
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
            @RequestParam String code, @RequestParam int semester, @RequestParam int credits,
            RedirectAttributes redirectAttributes) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            try {
                subjectRepository.save(new Subject(course, name, code, semester, credits));
                redirectAttributes.addFlashAttribute("success", "Subject added successfully!");
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("error", "Subject code already exists!");
            }
        }
        return "redirect:/admin/subjects";
    }

    @PostMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            subjectRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Subject deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete: Subject has allotments or attendance records!");
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
            @RequestParam int semester, @RequestParam int year, RedirectAttributes redirectAttributes) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            try {
                sectionRepository.save(new Section(course, name, semester, year));
                redirectAttributes.addFlashAttribute("success", "Section added successfully!");
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("error", "Section name already exists!");
            }
        }
        return "redirect:/admin/sections";
    }

    @PostMapping("/sections/delete/{id}")
    public String deleteSection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sectionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Section deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete: Section has students or allotments linked to it!");
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
            @RequestParam String role, @RequestParam(required = false) Long sectionId,
            RedirectAttributes redirectAttributes) {

        try {
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
            redirectAttributes.addFlashAttribute("success", "User added successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Username or email already exists!");
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // First find the user to check their role
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Prevent deletion of admin users
                if (user.getRole() == Role.ADMIN) {
                    redirectAttributes.addFlashAttribute("error", "Cannot delete admin users!");
                    return "redirect:/admin/users";
                }

                // If user is a student, delete their attendance records first, then the student
                Optional<Student> studentOpt = studentRepository.findByUser(user);
                if (studentOpt.isPresent()) {
                    Student student = studentOpt.get();
                    // Delete all attendance records for this student
                    List<Attendance> attendanceRecords = attendanceRepository.findByStudent(student);
                    attendanceRepository.deleteAll(attendanceRecords);
                    // Delete the student record
                    studentRepository.delete(student);
                }

                // If user is a teacher, delete their subject assignments first, then the
                // teacher
                Optional<Teacher> teacherOpt = teacherRepository.findByUser(user);
                if (teacherOpt.isPresent()) {
                    Teacher teacher = teacherOpt.get();
                    // Delete all teacher-subject assignments
                    List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findByTeacher(teacher);
                    teacherSubjectRepository.deleteAll(teacherSubjects);
                    // Delete the teacher record
                    teacherRepository.delete(teacher);
                }

                // Finally delete the user
                userRepository.delete(user);
                redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete user: Has linked records!");
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
            @RequestParam Long sectionId, RedirectAttributes redirectAttributes) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        Section section = sectionRepository.findById(sectionId).orElse(null);
        if (teacher != null && subject != null && section != null) {
            try {
                teacherSubjectRepository.save(new TeacherSubject(teacher, subject, section));
                redirectAttributes.addFlashAttribute("success", "Allotment added successfully!");
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("error", "This allotment already exists!");
            }
        }
        return "redirect:/admin/allotments";
    }

    @PostMapping("/allotments/delete/{id}")
    public String deleteAllotment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherSubjectRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Allotment deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete allotment!");
        }
        return "redirect:/admin/allotments";
    }
}
