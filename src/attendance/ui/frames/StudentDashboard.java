package attendance.ui.frames;

import attendance.database.DataStore;
import attendance.models.*;
import attendance.services.AuthService;
import attendance.ui.components.*;
import attendance.ui.panels.SettingsPanel;
import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Student Dashboard for viewing attendance and reports
 */
public class StudentDashboard extends JFrame {
    private SidebarPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DataStore dataStore;
    private Student currentStudent;
    private AttendanceRule attendanceRule;

    public StudentDashboard() {
        dataStore = DataStore.getInstance();
        User currentUser = dataStore.getCurrentUser();
        if (currentUser != null) {
            currentStudent = dataStore.getStudentByUserId(currentUser.getId());
        }
        attendanceRule = dataStore.getAttendanceRule();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Dashboard - University Attendance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeColors.BG_DARK);

        // Setup sidebar
        sidebar = new SidebarPanel();
        String studentName = currentStudent != null ? currentStudent.getName() : "Student";
        sidebar.setHeader("Student", studentName);
        sidebar.addItem("\uD83C\uDFE0", "Dashboard", "DASHBOARD");
        sidebar.addItem("\uD83D\uDCCA", "Subject Attendance", "SUBJECTS");
        sidebar.addItem("\uD83D\uDCC5", "Calendar View", "CALENDAR");
        sidebar.addItem("\uD83D\uDCE5", "Download Report", "DOWNLOAD");
        sidebar.addItem("\uD83C\uDFA8", "Settings", "SETTINGS");

        sidebar.setNavigationListener(e -> {
            String command = e.getActionCommand();
            if ("LOGOUT".equals(command)) {
                performLogout();
            } else {
                cardLayout.show(contentPanel, command);
            }
        });

        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content area with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ThemeColors.BG_DARK);

        // Add panels
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createSubjectsPanel(), "SUBJECTS");
        contentPanel.add(createCalendarPanel(), "CALENDAR");
        contentPanel.add(createDownloadPanel(), "DOWNLOAD");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Welcome, " + (currentStudent != null ? currentStudent.getName() : "Student"));
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Top section with overall progress and stats
        JPanel topSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        topSection.setOpaque(false);
        topSection.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Overall attendance circular progress
        JPanel progressPanel = new GradientPanel(true);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        progressPanel.setPreferredSize(new Dimension(220, 280));

        JLabel progressTitle = new JLabel("Overall Attendance");
        progressTitle.setFont(ThemeColors.FONT_SUBTITLE);
        progressTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        progressTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressPanel.add(progressTitle);
        progressPanel.add(Box.createVerticalStrut(15));

        double overallPercentage = currentStudent != null ? dataStore.calculateStudentAttendance(currentStudent.getId())
                : 0;

        CircularProgressBar progressBar = new CircularProgressBar();
        progressBar.setPreferredSize(new Dimension(160, 160));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set color based on attendance
        Color statusColor = CircularProgressBar.getStatusColor(
                overallPercentage,
                attendanceRule.getMinPercentage(),
                attendanceRule.getDetentionThreshold());
        progressBar.setProgressColor(statusColor);
        progressBar.setPercentage(overallPercentage);
        progressPanel.add(progressBar);

        progressPanel.add(Box.createVerticalStrut(15));

        // Status label
        String statusText = getStatusText(overallPercentage);
        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(ThemeColors.FONT_BUTTON);
        statusLabel.setForeground(statusColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressPanel.add(statusLabel);

        topSection.add(progressPanel);

        // Stats cards
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        // Get subject-wise data
        Section section = currentStudent != null ? dataStore.getSectionById(currentStudent.getSectionId()) : null;
        Course course = section != null ? dataStore.getCourseById(section.getCourseId()) : null;
        List<Subject> subjects = course != null ? dataStore.getSubjectsByCourse(course.getId()) : new ArrayList<>();

        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        cardsRow.setOpaque(false);

        cardsRow.add(new DashboardCard(
                "Total Subjects",
                String.valueOf(subjects.size()),
                "\uD83D\uDCDA",
                ThemeColors.ACCENT_CYAN));

        cardsRow.add(new DashboardCard(
                "Classes Attended",
                String.valueOf(getClassesAttended()),
                "\u2714",
                ThemeColors.ACCENT_GREEN));

        cardsRow.add(new DashboardCard(
                "Classes Missed",
                String.valueOf(getClassesMissed()),
                "\u2716",
                ThemeColors.STATUS_DANGER));

        statsPanel.add(cardsRow);
        topSection.add(statsPanel);

        mainContent.add(topSection, BorderLayout.NORTH);

        // Subject-wise breakdown
        JPanel subjectsPanel = new GradientPanel(true);
        subjectsPanel.setLayout(new BorderLayout());
        subjectsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel subjectsTitle = new JLabel("Subject-wise Attendance");
        subjectsTitle.setFont(ThemeColors.FONT_SUBTITLE);
        subjectsTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        subjectsPanel.add(subjectsTitle, BorderLayout.NORTH);

        // Subject progress bars
        JPanel barsPanel = new JPanel();
        barsPanel.setOpaque(false);
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        for (Subject subject : subjects) {
            double subjectPercentage = currentStudent != null
                    ? dataStore.calculateStudentSubjectAttendance(currentStudent.getId(), subject.getId())
                    : 0;

            JPanel subjectRow = createSubjectProgressRow(subject.getName(), subjectPercentage);
            barsPanel.add(subjectRow);
            barsPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane barsScroll = new JScrollPane(barsPanel);
        barsScroll.setOpaque(false);
        barsScroll.getViewport().setOpaque(false);
        barsScroll.setBorder(null);
        subjectsPanel.add(barsScroll, BorderLayout.CENTER);

        JPanel subjectsContainer = new JPanel(new BorderLayout());
        subjectsContainer.setOpaque(false);
        subjectsContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        subjectsContainer.add(subjectsPanel, BorderLayout.CENTER);

        mainContent.add(subjectsContainer, BorderLayout.CENTER);

        // Alerts section
        if (overallPercentage < attendanceRule.getMinPercentage()) {
            JPanel alertPanel = createAlertPanel(overallPercentage);
            mainContent.add(alertPanel, BorderLayout.SOUTH);
        }

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSubjectProgressRow(String subjectName, double percentage) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Subject name
        JLabel nameLabel = new JLabel(subjectName);
        nameLabel.setFont(ThemeColors.FONT_REGULAR);
        nameLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        nameLabel.setPreferredSize(new Dimension(200, 30));
        row.add(nameLabel, BorderLayout.WEST);

        // Progress bar
        JPanel progressContainer = new JPanel(new BorderLayout(10, 0));
        progressContainer.setOpaque(false);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) percentage);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setBackground(ThemeColors.BG_LIGHT);

        Color barColor = CircularProgressBar.getStatusColor(
                percentage,
                attendanceRule.getMinPercentage(),
                attendanceRule.getDetentionThreshold());
        progressBar.setForeground(barColor);

        progressContainer.add(progressBar, BorderLayout.CENTER);

        // Percentage label
        JLabel percentLabel = new JLabel(String.format("%.1f%%", percentage));
        percentLabel.setFont(ThemeColors.FONT_BUTTON);
        percentLabel.setForeground(barColor);
        percentLabel.setPreferredSize(new Dimension(60, 30));
        progressContainer.add(percentLabel, BorderLayout.EAST);

        // Status indicator
        String statusEmoji = percentage >= attendanceRule.getMinPercentage() ? "\uD83D\uDFE2"
                : percentage >= attendanceRule.getDetentionThreshold() ? "\uD83D\uDFE1" : "\uD83D\uDD34";
        JLabel statusIndicator = new JLabel(statusEmoji);
        statusIndicator.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        row.add(statusIndicator, BorderLayout.EAST);

        row.add(progressContainer, BorderLayout.CENTER);

        return row;
    }

    private JPanel createAlertPanel(double overallPercentage) {
        JPanel alertPanel = new JPanel(new BorderLayout());
        alertPanel.setOpaque(false);
        alertPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel alert = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        alert.setBackground(ThemeColors.withAlpha(ThemeColors.STATUS_WARNING, 40));
        alert.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColors.STATUS_WARNING, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel alertIcon = new JLabel("\u26A0");
        alertIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        alertIcon.setForeground(ThemeColors.STATUS_WARNING);
        alert.add(alertIcon);

        String alertText = String.format(
                "<html><b>Low Attendance Alert!</b><br>" +
                        "Your attendance (%.1f%%) is below the required minimum (%.0f%%).<br>" +
                        "Attend more classes to avoid detention.</html>",
                overallPercentage, attendanceRule.getMinPercentage());
        JLabel alertLabel = new JLabel(alertText);
        alertLabel.setFont(ThemeColors.FONT_REGULAR);
        alertLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        alert.add(alertLabel);

        alertPanel.add(alert, BorderLayout.CENTER);

        return alertPanel;
    }

    private JPanel createSubjectsPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Subject-wise Attendance Details");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Subjects table
        Section section = currentStudent != null ? dataStore.getSectionById(currentStudent.getSectionId()) : null;
        Course course = section != null ? dataStore.getCourseById(section.getCourseId()) : null;
        List<Subject> subjects = course != null ? dataStore.getSubjectsByCourse(course.getId()) : new ArrayList<>();

        String[] columns = { "Subject", "Code", "Credits", "Classes Held", "Classes Attended", "Attendance %",
                "Status" };
        Object[][] data = new Object[subjects.size()][7];

        for (int i = 0; i < subjects.size(); i++) {
            Subject s = subjects.get(i);
            double percentage = currentStudent != null
                    ? dataStore.calculateStudentSubjectAttendance(currentStudent.getId(), s.getId())
                    : 0;

            List<Attendance> subjectRecords = currentStudent != null
                    ? dataStore.getAttendanceByStudentAndSubject(currentStudent.getId(), s.getId())
                    : new ArrayList<>();

            int classesHeld = subjectRecords.size();
            long classesAttended = subjectRecords.stream()
                    .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                    .count();

            String status = percentage >= attendanceRule.getMinPercentage() ? "Safe"
                    : percentage >= attendanceRule.getDetentionThreshold() ? "Warning" : "Detention Risk";

            data[i] = new Object[] {
                    s.getName(),
                    s.getCode(),
                    s.getCredits(),
                    classesHeld,
                    classesAttended,
                    String.format("%.1f%%", percentage),
                    status
            };
        }

        JTable table = createStyledTable(columns, data);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_MEDIUM);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Attendance Calendar");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Month navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);

        GlowButton prevBtn = new GlowButton("<", ThemeColors.ACCENT_CYAN);
        prevBtn.setPreferredSize(new Dimension(50, 35));
        navPanel.add(prevBtn);

        JLabel monthLabel = new JLabel(YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        monthLabel.setFont(ThemeColors.FONT_SUBTITLE);
        monthLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        navPanel.add(monthLabel);

        GlowButton nextBtn = new GlowButton(">", ThemeColors.ACCENT_CYAN);
        nextBtn.setPreferredSize(new Dimension(50, 35));
        navPanel.add(nextBtn);

        headerPanel.add(navPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Calendar grid
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setOpaque(false);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Day headers
        String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        JPanel dayHeaderPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        dayHeaderPanel.setOpaque(false);

        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(ThemeColors.FONT_BUTTON);
            dayLabel.setForeground(ThemeColors.TEXT_MUTED);
            dayHeaderPanel.add(dayLabel);
        }
        calendarPanel.add(dayHeaderPanel, BorderLayout.NORTH);

        // Calendar days grid
        JPanel daysGrid = new JPanel(new GridLayout(6, 7, 5, 5));
        daysGrid.setOpaque(false);
        daysGrid.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        YearMonth currentMonth = YearMonth.now();
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        // Empty cells before first day
        for (int i = 0; i < dayOfWeek; i++) {
            daysGrid.add(new JLabel(""));
        }

        // Days with attendance status from real data
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayCell = createDayCell(day);
            daysGrid.add(dayCell);
        }

        // Fill remaining cells
        int remainingCells = 42 - dayOfWeek - daysInMonth;
        for (int i = 0; i < remainingCells; i++) {
            daysGrid.add(new JLabel(""));
        }

        calendarPanel.add(daysGrid, BorderLayout.CENTER);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        legendPanel.add(createLegendItem("\uD83D\uDFE2 Present", ThemeColors.STATUS_SAFE));
        legendPanel.add(createLegendItem("\uD83D\uDD34 Absent", ThemeColors.STATUS_DANGER));
        legendPanel.add(createLegendItem("\uD83D\uDFE1 Late", ThemeColors.STATUS_WARNING));
        legendPanel.add(createLegendItem("\u26AA Weekend/Holiday", ThemeColors.TEXT_MUTED));

        calendarPanel.add(legendPanel, BorderLayout.SOUTH);

        panel.add(calendarPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDayCell(int day) {
        JPanel cell = new GradientPanel(true);
        cell.setLayout(new BorderLayout());
        cell.setPreferredSize(new Dimension(80, 60));

        LocalDate date = YearMonth.now().atDay(day);
        boolean isWeekend = date.getDayOfWeek().getValue() > 5;
        boolean isFuture = date.isAfter(LocalDate.now());

        JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
        dayLabel.setFont(ThemeColors.FONT_REGULAR);

        if (isWeekend || isFuture) {
            dayLabel.setForeground(ThemeColors.TEXT_MUTED);
        } else if (currentStudent != null) {
            // Get actual attendance for this date
            List<Attendance> dayAttendance = dataStore.getAttendanceByStudent(currentStudent.getId())
                    .stream().filter(a -> a.getDate().equals(date)).toList();

            if (dayAttendance.isEmpty()) {
                // No attendance data for this day
                dayLabel.setForeground(ThemeColors.TEXT_MUTED);
            } else {
                // Determine status color based on attendance records
                long presentCount = dayAttendance.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
                long absentCount = dayAttendance.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
                long lateCount = dayAttendance.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();

                Color statusColor;
                if (absentCount > 0) {
                    statusColor = ThemeColors.STATUS_DANGER; // Any absent = red
                } else if (lateCount > 0) {
                    statusColor = ThemeColors.STATUS_WARNING; // Any late = yellow
                } else if (presentCount > 0) {
                    statusColor = ThemeColors.STATUS_SAFE; // All present = green
                } else {
                    statusColor = ThemeColors.TEXT_MUTED; // Fallback
                }

                dayLabel.setForeground(ThemeColors.TEXT_PRIMARY);
                cell.setBackground(ThemeColors.withAlpha(statusColor, 50));
            }
        } else {
            dayLabel.setForeground(ThemeColors.TEXT_MUTED);
        }

        cell.add(dayLabel, BorderLayout.CENTER);

        return cell;
    }

    private JLabel createLegendItem(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(ThemeColors.FONT_SMALL);
        label.setForeground(color);
        return label;
    }

    private JPanel createDownloadPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Download Attendance Report");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Download options
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        // PDF option
        JPanel pdfCard = new GradientPanel(true);
        pdfCard.setLayout(new BoxLayout(pdfCard, BoxLayout.Y_AXIS));
        pdfCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        pdfCard.setPreferredSize(new Dimension(250, 200));

        JLabel pdfIcon = new JLabel("\uD83D\uDCC4");
        pdfIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        pdfIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfCard.add(pdfIcon);

        pdfCard.add(Box.createVerticalStrut(15));

        JLabel pdfTitle = new JLabel("PDF Report");
        pdfTitle.setFont(ThemeColors.FONT_SUBTITLE);
        pdfTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        pdfTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfCard.add(pdfTitle);

        pdfCard.add(Box.createVerticalStrut(10));

        JLabel pdfDesc = new JLabel("Complete attendance report");
        pdfDesc.setFont(ThemeColors.FONT_SMALL);
        pdfDesc.setForeground(ThemeColors.TEXT_MUTED);
        pdfDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfCard.add(pdfDesc);

        pdfCard.add(Box.createVerticalStrut(20));

        GlowButton pdfBtn = new GlowButton("Download PDF", ThemeColors.ACCENT_CYAN);
        pdfBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Generating PDF report...\nThis would download the attendance report as PDF.",
                    "Download",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        pdfCard.add(pdfBtn);

        optionsPanel.add(pdfCard);

        // CSV option
        JPanel csvCard = new GradientPanel(true);
        csvCard.setLayout(new BoxLayout(csvCard, BoxLayout.Y_AXIS));
        csvCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        csvCard.setPreferredSize(new Dimension(250, 200));

        JLabel csvIcon = new JLabel("\uD83D\uDCCA");
        csvIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        csvIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        csvCard.add(csvIcon);

        csvCard.add(Box.createVerticalStrut(15));

        JLabel csvTitle = new JLabel("CSV Export");
        csvTitle.setFont(ThemeColors.FONT_SUBTITLE);
        csvTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        csvTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        csvCard.add(csvTitle);

        csvCard.add(Box.createVerticalStrut(10));

        JLabel csvDesc = new JLabel("Raw data for spreadsheets");
        csvDesc.setFont(ThemeColors.FONT_SMALL);
        csvDesc.setForeground(ThemeColors.TEXT_MUTED);
        csvDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        csvCard.add(csvDesc);

        csvCard.add(Box.createVerticalStrut(20));

        GlowButton csvBtn = new GlowButton("Download CSV", ThemeColors.ACCENT_GREEN);
        csvBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        csvBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Exporting CSV data...\nThis would download the attendance data as CSV.",
                    "Export",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        csvCard.add(csvBtn);

        optionsPanel.add(csvCard);

        panel.add(optionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JTable createStyledTable(String[] columns, Object[][] data) {
        JTable table = new JTable(data, columns);
        table.setBackground(ThemeColors.BG_MEDIUM);
        table.setForeground(ThemeColors.TEXT_PRIMARY);
        table.setFont(ThemeColors.FONT_REGULAR);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 50));
        table.setSelectionForeground(ThemeColors.TEXT_PRIMARY);

        // Header styling
        table.getTableHeader().setBackground(ThemeColors.BG_LIGHT);
        table.getTableHeader().setForeground(ThemeColors.TEXT_SECONDARY);
        table.getTableHeader().setFont(ThemeColors.FONT_BUTTON);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));

        return table;
    }

    private String getStatusText(double percentage) {
        if (percentage >= attendanceRule.getMinPercentage()) {
            return "SAFE";
        } else if (percentage >= attendanceRule.getDetentionThreshold()) {
            return "WARNING";
        } else {
            return "DETENTION RISK";
        }
    }

    private int getClassesAttended() {
        if (currentStudent == null)
            return 0;
        List<Attendance> records = dataStore.getAttendanceByStudent(currentStudent.getId());
        return (int) records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();
    }

    private int getClassesMissed() {
        if (currentStudent == null)
            return 0;
        List<Attendance> records = dataStore.getAttendanceByStudent(currentStudent.getId());
        return (int) records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT)
                .count();
    }

    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            AuthService.getInstance().logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
