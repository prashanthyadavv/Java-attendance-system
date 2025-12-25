package com.attendance.controller;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;

    public StudentController(
            UserRepository userRepository,
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository,
            TeacherSubjectRepository teacherSubjectRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
    }

    private Student getCurrentStudent(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user != null) {
            return studentRepository.findByUser(user).orElse(null);
        }
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Student student = getCurrentStudent(userDetails);
        if (student == null) {
            return "redirect:/login";
        }

        List<Attendance> allAttendance = attendanceRepository.findByStudent(student);

        // Calculate statistics
        long totalClasses = allAttendance.size();
        long presentCount = allAttendance.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absentCount = allAttendance.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long lateCount = allAttendance.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count();

        double attendancePercentage = totalClasses > 0 ? (double) (presentCount + lateCount) / totalClasses * 100 : 0;

        // Get subject-wise breakdown
        Map<String, Map<String, Long>> subjectWise = new LinkedHashMap<>();
        for (Attendance att : allAttendance) {
            String subjectName = att.getSubject().getName();
            subjectWise.computeIfAbsent(subjectName, k -> new HashMap<>());
            Map<String, Long> stats = subjectWise.get(subjectName);
            stats.merge("total", 1L, Long::sum);
            if (att.getStatus() == AttendanceStatus.PRESENT || att.getStatus() == AttendanceStatus.LATE) {
                stats.merge("present", 1L, Long::sum);
            }
        }

        model.addAttribute("studentName", student.getName());
        model.addAttribute("rollNumber", student.getRollNumber());
        model.addAttribute("section", student.getSection().getName());
        model.addAttribute("totalClasses", totalClasses);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        model.addAttribute("lateCount", lateCount);
        model.addAttribute("attendancePercentage", String.format("%.1f", attendancePercentage));
        model.addAttribute("subjectWise", subjectWise);
        model.addAttribute("recentAttendance", allAttendance.stream().limit(10).toList());

        // Detention warning
        boolean inDanger = attendancePercentage < 75;
        model.addAttribute("inDanger", inDanger);

        return "student/dashboard";
    }

    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Student student = getCurrentStudent(userDetails);
        if (student == null) {
            return "redirect:/login";
        }

        // Get attendance for current month
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        List<Attendance> monthlyAttendance = attendanceRepository.findByStudentAndDateBetween(student, startOfMonth,
                endOfMonth);

        // Create calendar data
        Map<Integer, String> calendarData = new HashMap<>();
        for (Attendance att : monthlyAttendance) {
            int day = att.getDate().getDayOfMonth();
            String status = att.getStatus().name();
            calendarData.put(day, status);
        }

        model.addAttribute("studentName", student.getName());
        model.addAttribute("currentMonth", now.getMonth().toString());
        model.addAttribute("currentYear", now.getYear());
        model.addAttribute("calendarData", calendarData);
        model.addAttribute("daysInMonth", now.lengthOfMonth());
        model.addAttribute("firstDayOfWeek", startOfMonth.getDayOfWeek().getValue());

        return "student/calendar";
    }
}
