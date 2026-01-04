package com.attendance.controller;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;

    public TeacherController(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository,
            SubjectRepository subjectRepository,
            SectionRepository sectionRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.subjectRepository = subjectRepository;
        this.sectionRepository = sectionRepository;
    }

    private Teacher getCurrentTeacher(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user != null) {
            return teacherRepository.findByUser(user).orElse(null);
        }
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        List<TeacherSubject> assignments = teacherSubjectRepository.findByTeacher(teacher);

        // Count unique sections and subjects
        long totalSections = assignments.stream().map(TeacherSubject::getSection).distinct().count();
        long totalSubjects = assignments.stream().map(TeacherSubject::getSubject).distinct().count();

        // Count students in assigned sections
        long totalStudents = assignments.stream()
                .map(ts -> studentRepository.findBySection(ts.getSection()).size())
                .mapToLong(Integer::longValue).sum();

        model.addAttribute("teacherName", teacher.getName());
        model.addAttribute("totalSections", totalSections);
        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("assignments", assignments);

        return "teacher/dashboard";
    }

    @GetMapping("/mark-attendance")
    public String markAttendance(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        List<TeacherSubject> assignments = teacherSubjectRepository.findByTeacher(teacher);
        List<Section> sections = assignments.stream().map(TeacherSubject::getSection).distinct()
                .collect(Collectors.toList());
        List<Subject> subjects = assignments.stream().map(TeacherSubject::getSubject).distinct()
                .collect(Collectors.toList());

        model.addAttribute("sections", sections);
        model.addAttribute("subjects", subjects);
        model.addAttribute("today", LocalDate.now());

        return "teacher/mark-attendance";
    }

    @GetMapping("/students/{sectionId}")
    @ResponseBody
    public List<Student> getStudentsBySection(@PathVariable Long sectionId) {
        Section section = new Section();
        section.setId(sectionId);
        return studentRepository.findBySection(section);
    }

    @GetMapping("/students/{sectionId}/attendance")
    @ResponseBody
    public List<Map<String, Object>> getStudentsWithAttendance(
            @PathVariable Long sectionId,
            @RequestParam Long subjectId,
            @RequestParam String date) {

        Section section = new Section();
        section.setId(sectionId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        LocalDate attendanceDate = LocalDate.parse(date);

        List<Student> students = studentRepository.findBySection(section);
        List<Attendance> existingAttendance = attendanceRepository.findBySubjectAndDateAndSection(subject,
                attendanceDate, section);

        // Create a map of student ID to attendance status
        Map<Long, String> attendanceMap = new HashMap<>();
        for (Attendance a : existingAttendance) {
            attendanceMap.put(a.getStudent().getId(), a.getStatus().name());
        }

        // Return students with their existing status
        return students.stream().map(s -> {
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("id", s.getId());
            studentData.put("name", s.getName());
            studentData.put("rollNumber", s.getRollNumber());
            studentData.put("status", attendanceMap.getOrDefault(s.getId(), "PRESENT"));
            return studentData;
        }).collect(Collectors.toList());
    }

    @PostMapping("/mark-attendance")
    public String saveAttendance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long subjectId,
            @RequestParam Long sectionId,
            @RequestParam LocalDate date,
            @RequestParam List<Long> studentIds,
            @RequestParam List<String> statuses) {

        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        // Validate date - prevent future dates
        if (date.isAfter(LocalDate.now())) {
            return "redirect:/teacher/mark-attendance?error=future";
        }

        User markedBy = teacher.getUser();
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            return "redirect:/teacher/mark-attendance?error=invalid";
        }

        for (int i = 0; i < studentIds.size(); i++) {
            Student student = studentRepository.findById(studentIds.get(i)).orElse(null);
            if (student == null)
                continue;

            AttendanceStatus status = AttendanceStatus.valueOf(statuses.get(i));

            // Check for existing attendance and update instead of creating duplicate
            List<Attendance> existing = attendanceRepository.findByStudentAndSubjectAndDate(student, subject, date);
            if (!existing.isEmpty()) {
                // Update existing
                Attendance att = existing.get(0);
                att.setStatus(status);
                att.setMarkedBy(markedBy);
                att.setLateEntry(status == AttendanceStatus.LATE);
                attendanceRepository.save(att);
            } else {
                // Create new
                Attendance attendance = new Attendance(student, subject, date, 1, status, markedBy,
                        status == AttendanceStatus.LATE);
                attendanceRepository.save(attendance);
            }
        }

        return "redirect:/teacher/dashboard?success=true";
    }

    @GetMapping("/reports")
    public String reports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        List<TeacherSubject> assignments = teacherSubjectRepository.findByTeacher(teacher);
        model.addAttribute("assignments", assignments);

        return "teacher/reports";
    }

    @GetMapping("/reports/{subjectId}/{sectionId}")
    public String reportDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long subjectId,
            @PathVariable Long sectionId,
            Model model) {

        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        Section section = sectionRepository.findById(sectionId).orElse(null);

        if (subject == null || section == null) {
            return "redirect:/teacher/reports";
        }

        // Get all students in the section
        List<Student> students = studentRepository.findBySection(section);

        // Calculate attendance statistics for each student
        List<Map<String, Object>> studentStats = new ArrayList<>();
        for (Student student : students) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("id", student.getId());
            stats.put("name", student.getName());
            stats.put("rollNumber", student.getRollNumber());

            long total = attendanceRepository.countByStudentAndSubject(student, subject);
            long present = attendanceRepository.countByStudentAndSubjectAndStatus(student, subject,
                    AttendanceStatus.PRESENT);
            long absent = attendanceRepository.countByStudentAndSubjectAndStatus(student, subject,
                    AttendanceStatus.ABSENT);
            long late = attendanceRepository.countByStudentAndSubjectAndStatus(student, subject, AttendanceStatus.LATE);

            stats.put("totalClasses", total);
            stats.put("present", present);
            stats.put("absent", absent);
            stats.put("late", late);

            // Calculate percentage (present + late count as attended)
            double percentage = total > 0 ? ((present + late) * 100.0 / total) : 0;
            stats.put("percentage", Math.round(percentage * 10) / 10.0);

            studentStats.add(stats);
        }

        model.addAttribute("subject", subject);
        model.addAttribute("section", section);
        model.addAttribute("studentStats", studentStats);
        model.addAttribute("totalStudents", students.size());

        return "teacher/report-detail";
    }

    @GetMapping("/reports/student/{studentId}/{subjectId}")
    public String studentAttendanceDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long studentId,
            @PathVariable Long subjectId,
            Model model) {

        Teacher teacher = getCurrentTeacher(userDetails);
        if (teacher == null) {
            return "redirect:/login";
        }

        Student student = studentRepository.findById(studentId).orElse(null);
        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if (student == null || subject == null) {
            return "redirect:/teacher/reports";
        }

        // Get all attendance records for this student and subject
        List<Attendance> attendanceRecords = attendanceRepository.findByStudentAndSubject(student, subject);

        // Sort by date descending
        attendanceRecords.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        // Calculate statistics
        long total = attendanceRecords.size();
        long present = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absent = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long late = attendanceRecords.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        double percentage = total > 0 ? ((present + late) * 100.0 / total) : 0;

        model.addAttribute("student", student);
        model.addAttribute("subject", subject);
        model.addAttribute("attendanceRecords", attendanceRecords);
        model.addAttribute("totalClasses", total);
        model.addAttribute("present", present);
        model.addAttribute("absent", absent);
        model.addAttribute("late", late);
        model.addAttribute("percentage", Math.round(percentage * 10) / 10.0);

        return "teacher/student-attendance";
    }
}
