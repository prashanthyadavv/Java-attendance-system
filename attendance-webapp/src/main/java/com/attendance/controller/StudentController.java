package com.attendance.controller;

import com.attendance.model.*;
import com.attendance.repository.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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
            stats.put("total", stats.getOrDefault("total", 0L) + 1L);
            if (att.getStatus() == AttendanceStatus.PRESENT || att.getStatus() == AttendanceStatus.LATE) {
                stats.put("present", stats.getOrDefault("present", 0L) + 1L);
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
    public String calendar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {

        Student student = getCurrentStudent(userDetails);
        if (student == null) {
            return "redirect:/login";
        }

        // Use provided month/year or default to current
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null && month >= 1 && month <= 12) ? month : now.getMonthValue();
        int selectedYear = (year != null && year >= 2020 && year <= 2030) ? year : now.getYear();

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<Attendance> monthlyAttendance = attendanceRepository.findByStudentAndDateBetween(student, startOfMonth,
                endOfMonth);

        // Sort by date descending for the list
        List<Attendance> sortedAttendance = monthlyAttendance.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        // Create calendar data with subject-wise attendance
        // Map<Day, Map<SubjectName, Status>>
        Map<Integer, Map<String, String>> calendarData = new LinkedHashMap<>();

        for (Attendance att : monthlyAttendance) {
            int day = att.getDate().getDayOfMonth();
            String subjectName = att.getSubject().getName();
            String status = att.getStatus().name();

            calendarData.computeIfAbsent(day, k -> new LinkedHashMap<>());
            calendarData.get(day).put(subjectName, status);
        }

        // Get all unique subjects for the student's section
        List<TeacherSubject> assignments = teacherSubjectRepository.findAll().stream()
                .filter(ts -> ts.getSection().getId().equals(student.getSection().getId()))
                .collect(Collectors.toList());

        List<String> subjects = assignments.stream()
                .map(ts -> ts.getSubject().getName())
                .distinct()
                .collect(Collectors.toList());

        // Calculate previous and next month for navigation
        YearMonth prevMonth = yearMonth.minusMonths(1);
        YearMonth nextMonth = yearMonth.plusMonths(1);

        // Calculate monthly stats
        long monthlyPresent = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long monthlyAbsent = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long monthlyLate = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        long monthlyTotal = monthlyAttendance.size();

        model.addAttribute("studentName", student.getName());
        model.addAttribute("currentMonth", yearMonth.getMonth().toString());
        model.addAttribute("currentYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("calendarData", calendarData);
        model.addAttribute("subjects", subjects);
        model.addAttribute("daysInMonth", yearMonth.lengthOfMonth());
        model.addAttribute("firstDayOfWeek", startOfMonth.getDayOfWeek().getValue());

        // Navigation
        model.addAttribute("prevMonth", prevMonth.getMonthValue());
        model.addAttribute("prevYear", prevMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());

        // For dropdown
        model.addAttribute("months", getMonthsList());
        model.addAttribute("years", getYearsList());

        // Detailed list
        model.addAttribute("attendanceList", sortedAttendance);
        model.addAttribute("monthlyPresent", monthlyPresent);
        model.addAttribute("monthlyAbsent", monthlyAbsent);
        model.addAttribute("monthlyLate", monthlyLate);
        model.addAttribute("monthlyTotal", monthlyTotal);

        return "student/calendar";
    }

    private List<Map<String, Object>> getMonthsList() {
        List<Map<String, Object>> months = new ArrayList<>();
        String[] monthNames = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("value", i);
            m.put("name", monthNames[i - 1]);
            months.add(m);
        }
        return months;
    }

    private List<Integer> getYearsList() {
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 2; y <= currentYear + 1; y++) {
            years.add(y);
        }
        return years;
    }
}
