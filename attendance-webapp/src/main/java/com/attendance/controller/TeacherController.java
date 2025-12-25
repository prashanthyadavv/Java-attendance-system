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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public TeacherController(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
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

        User markedBy = teacher.getUser();
        Subject subject = new Subject();
        subject.setId(subjectId);

        for (int i = 0; i < studentIds.size(); i++) {
            Student student = new Student();
            student.setId(studentIds.get(i));

            AttendanceStatus status = AttendanceStatus.valueOf(statuses.get(i));
            Attendance attendance = new Attendance(student, subject, date, 1, status, markedBy,
                    status == AttendanceStatus.LATE);
            attendanceRepository.save(attendance);
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
}
