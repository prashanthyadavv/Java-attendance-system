package attendance.ui.frames;

import attendance.database.DataStore;
import attendance.models.*;
import attendance.services.AuthService;
import attendance.ui.components.*;
import attendance.ui.panels.SettingsPanel;
import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Teacher Dashboard for attendance marking and class management
 */
public class TeacherDashboard extends JFrame {
    private SidebarPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DataStore dataStore;
    private Teacher currentTeacher;

    public TeacherDashboard() {
        dataStore = DataStore.getInstance();
        User currentUser = dataStore.getCurrentUser();
        if (currentUser != null) {
            currentTeacher = dataStore.getTeacherByUserId(currentUser.getId());
        }
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Teacher Dashboard - University Attendance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeColors.BG_DARK);

        // Setup sidebar
        sidebar = new SidebarPanel();
        String teacherName = currentTeacher != null ? currentTeacher.getName() : "Teacher";
        sidebar.setHeader("Teacher", teacherName);
        sidebar.addItem("\uD83C\uDFE0", "Dashboard", "DASHBOARD");
        sidebar.addItem("\u2714", "Mark Attendance", "MARK_ATTENDANCE");
        sidebar.addItem("\uD83D\uDCDD", "Edit Attendance", "EDIT_ATTENDANCE");
        sidebar.addItem("\uD83D\uDCCA", "Reports", "REPORTS");
        sidebar.addItem("\uD83D\uDC65", "My Classes", "CLASSES");
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
        contentPanel.add(createMarkAttendancePanel(), "MARK_ATTENDANCE");
        contentPanel.add(createEditAttendancePanel(), "EDIT_ATTENDANCE");
        contentPanel.add(createReportsPanel(), "REPORTS");
        contentPanel.add(createClassesPanel(), "CLASSES");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Welcome, " + (currentTeacher != null ? currentTeacher.getName() : "Teacher"));
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Stats cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Get teacher's classes count
        List<TeacherSubject> assignments = currentTeacher != null ? dataStore.getTeacherSubjects(currentTeacher.getId())
                : new ArrayList<>();

        cardsPanel.add(new DashboardCard(
                "My Subjects",
                String.valueOf(getUniqueSubjectsCount(assignments)),
                "\uD83D\uDCDA",
                ThemeColors.ACCENT_CYAN));

        cardsPanel.add(new DashboardCard(
                "My Sections",
                String.valueOf(getUniqueSectionsCount(assignments)),
                "\uD83D\uDC65",
                ThemeColors.ACCENT_PURPLE));

        cardsPanel.add(new DashboardCard(
                "Today's Classes",
                String.valueOf(assignments.size()),
                "\uD83D\uDCC5",
                ThemeColors.ACCENT_BLUE));

        // Calculate pending classes for today
        int pendingCount = 0;
        for (TeacherSubject ts : assignments) {
            boolean markedToday = dataStore.getAttendanceBySubject(ts.getSubjectId()).stream()
                    .anyMatch(a -> a.getDate().equals(LocalDate.now()));
            if (!markedToday)
                pendingCount++;
        }

        cardsPanel.add(new DashboardCard(
                "Pending Today",
                String.valueOf(pendingCount),
                "\u26A0",
                ThemeColors.STATUS_WARNING));

        mainContent.add(cardsPanel, BorderLayout.NORTH);

        // Today's schedule
        JPanel schedulePanel = new GradientPanel(true);
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel scheduleTitle = new JLabel(
                "Today's Schedule - " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        scheduleTitle.setFont(ThemeColors.FONT_SUBTITLE);
        scheduleTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        schedulePanel.add(scheduleTitle, BorderLayout.NORTH);

        // Schedule table - dynamically load from teacher's subject assignments
        String[] columns = { "Period", "Subject", "Section", "Time", "Status" };

        // Get teacher's assigned subjects/sections
        List<TeacherSubject> teacherAssignments = currentTeacher != null
                ? dataStore.getTeacherSubjects(currentTeacher.getId())
                : new ArrayList<>();

        List<Object[]> scheduleRows = new ArrayList<>();
        int period = 1;
        String[] times = { "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 1:00", "2:00 - 3:00",
                "3:00 - 4:00" };

        for (TeacherSubject ts : teacherAssignments) {
            Subject subject = dataStore.getSubjectById(ts.getSubjectId());
            Section section = dataStore.getSectionById(ts.getSectionId());
            String time = period <= times.length ? times[period - 1] : "-";

            // Check if attendance was marked today for this subject/section
            boolean markedToday = dataStore.getAttendanceBySubject(ts.getSubjectId()).stream()
                    .anyMatch(a -> a.getDate().equals(LocalDate.now()));

            scheduleRows.add(new Object[] {
                    String.valueOf(period++),
                    subject != null ? subject.getName() : "-",
                    section != null ? section.getName() : "-",
                    time,
                    markedToday ? "Completed" : "Pending"
            });
        }

        Object[][] scheduleData = scheduleRows.isEmpty()
                ? new Object[][] { { "", "No subjects assigned", "", "", "" } }
                : scheduleRows.toArray(new Object[0][]);

        JTable scheduleTable = createStyledTable(columns, scheduleData);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_MEDIUM);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        schedulePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel scheduleContainer = new JPanel(new BorderLayout());
        scheduleContainer.setOpaque(false);
        scheduleContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        scheduleContainer.add(schedulePanel, BorderLayout.CENTER);

        mainContent.add(scheduleContainer, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMarkAttendancePanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Mark Attendance");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Selection area
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Section dropdown - load only teacher's assigned sections
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        sectionLabel.setFont(ThemeColors.FONT_REGULAR);
        selectionPanel.add(sectionLabel);

        JComboBox<Section> sectionCombo = new JComboBox<>();
        // Get teacher's assigned sections only
        List<TeacherSubject> teacherAssignments = currentTeacher != null
                ? dataStore.getTeacherSubjects(currentTeacher.getId())
                : new ArrayList<>();
        Set<Integer> teacherSectionIds = new java.util.HashSet<>();
        Set<Integer> teacherSubjectIds = new java.util.HashSet<>();
        for (TeacherSubject ts : teacherAssignments) {
            teacherSectionIds.add(ts.getSectionId());
            teacherSubjectIds.add(ts.getSubjectId());
        }

        List<Section> allSections = dataStore.getAllSections();
        for (Section s : allSections) {
            if (teacherSectionIds.contains(s.getId())) {
                sectionCombo.addItem(s);
            }
        }
        // If no assigned sections, show all (fallback)
        if (sectionCombo.getItemCount() == 0) {
            for (Section s : allSections) {
                sectionCombo.addItem(s);
            }
        }
        sectionCombo.setPreferredSize(new Dimension(150, 35));
        selectionPanel.add(sectionCombo);

        // Subject dropdown - load only teacher's assigned subjects
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        subjectLabel.setFont(ThemeColors.FONT_REGULAR);
        selectionPanel.add(subjectLabel);

        JComboBox<Subject> subjectCombo = new JComboBox<>();
        List<Subject> allSubjects = dataStore.getAllSubjects();
        for (Subject s : allSubjects) {
            if (teacherSubjectIds.contains(s.getId())) {
                subjectCombo.addItem(s);
            }
        }
        // If no assigned subjects, show all (fallback)
        if (subjectCombo.getItemCount() == 0) {
            for (Subject s : allSubjects) {
                subjectCombo.addItem(s);
            }
        }
        subjectCombo.setPreferredSize(new Dimension(200, 35));
        selectionPanel.add(subjectCombo);

        // Date picker (simplified)
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        dateLabel.setFont(ThemeColors.FONT_REGULAR);
        selectionPanel.add(dateLabel);

        JTextField dateField = new JTextField(LocalDate.now().toString());
        dateField.setPreferredSize(new Dimension(120, 35));
        selectionPanel.add(dateField);

        // Period dropdown
        JLabel periodLabel = new JLabel("Period:");
        periodLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        periodLabel.setFont(ThemeColors.FONT_REGULAR);
        selectionPanel.add(periodLabel);

        JComboBox<Integer> periodCombo = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        periodCombo.setPreferredSize(new Dimension(80, 35));
        selectionPanel.add(periodCombo);

        GlowButton loadBtn = new GlowButton("Load Students", ThemeColors.ACCENT_CYAN);
        selectionPanel.add(loadBtn);

        // Student attendance list
        JPanel attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setOpaque(false);
        attendancePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] columns = { "", "Roll No", "Name", "Status", "Late Entry" };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 4)
                    return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3 || column == 4;
            }
        };

        JTable attendanceTable = new JTable(model);
        attendanceTable.setBackground(ThemeColors.BG_MEDIUM);
        attendanceTable.setForeground(ThemeColors.TEXT_PRIMARY);
        attendanceTable.setFont(ThemeColors.FONT_REGULAR);
        attendanceTable.setRowHeight(45);
        attendanceTable.setShowGrid(false);
        attendanceTable.setSelectionBackground(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 50));

        // Column for status dropdown
        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Present", "Absent", "Late" });
        attendanceTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

        // Header styling
        attendanceTable.getTableHeader().setBackground(ThemeColors.BG_LIGHT);
        attendanceTable.getTableHeader().setForeground(ThemeColors.TEXT_SECONDARY);
        attendanceTable.getTableHeader().setFont(ThemeColors.FONT_BUTTON);
        attendanceTable.getTableHeader().setPreferredSize(new Dimension(0, 45));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_MEDIUM);

        attendancePanel.add(scrollPane, BorderLayout.CENTER);

        // Load Students button action - refresh table based on selected section
        loadBtn.addActionListener(e -> {
            Section selectedSection = (Section) sectionCombo.getSelectedItem();
            if (selectedSection != null) {
                // Clear existing rows
                model.setRowCount(0);

                // Get students for selected section
                List<Student> sectionStudents = dataStore.getStudentsBySection(selectedSection.getId());

                // Add students to table
                for (Student s : sectionStudents) {
                    model.addRow(
                            new Object[] { Boolean.TRUE, s.getRollNumber(), s.getName(), "Present", Boolean.FALSE });
                }

                if (sectionStudents.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No students found in section: " + selectedSection.getName(),
                            "No Students", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Auto-load students for the first section in combo (teacher's assigned
        // sections)
        if (sectionCombo.getItemCount() > 0) {
            Section firstSection = sectionCombo.getItemAt(0);
            List<Student> firstSectionStudents = dataStore.getStudentsBySection(firstSection.getId());
            for (Student s : firstSectionStudents) {
                model.addRow(new Object[] { Boolean.TRUE, s.getRollNumber(), s.getName(), "Present", Boolean.FALSE });
            }
        }

        // Action buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonsPanel.setOpaque(false);

        GlowButton markAllBtn = new GlowButton("Mark All Present", ThemeColors.ACCENT_GREEN);
        markAllBtn.addActionListener(e -> {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(Boolean.TRUE, i, 0);
                model.setValueAt("Present", i, 3);
            }
        });
        buttonsPanel.add(markAllBtn);

        GlowButton clearAllBtn = new GlowButton("Clear All", ThemeColors.STATUS_WARNING);
        clearAllBtn.addActionListener(e -> {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(Boolean.FALSE, i, 0);
                model.setValueAt("Absent", i, 3);
            }
        });
        buttonsPanel.add(clearAllBtn);

        GlowButton submitBtn = new GlowButton("Submit Attendance", ThemeColors.ACCENT_CYAN);
        submitBtn.addActionListener(e -> {
            Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
            if (selectedSubject == null) {
                JOptionPane.showMessageDialog(this, "Please select a subject!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int period = (Integer) periodCombo.getSelectedItem();
            int markedBy = currentTeacher != null ? currentTeacher.getId() : 0;

            int presentCount = 0;
            int absentCount = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                String rollNumber = (String) model.getValueAt(i, 1);
                String statusStr = (String) model.getValueAt(i, 3);
                boolean lateEntry = (Boolean) model.getValueAt(i, 4);

                // Map status string to AttendanceStatus enum
                AttendanceStatus status;
                if ("Absent".equals(statusStr)) {
                    status = AttendanceStatus.ABSENT;
                    absentCount++;
                } else if ("Late".equals(statusStr)) {
                    status = AttendanceStatus.LATE;
                    presentCount++;
                } else {
                    status = AttendanceStatus.PRESENT;
                    presentCount++;
                }

                // Find student and add attendance record
                Student student = dataStore.getStudentByRollNumber(rollNumber);
                if (student != null) {
                    dataStore.addAttendance(
                            student.getId(),
                            selectedSubject.getId(),
                            date,
                            period,
                            status,
                            markedBy,
                            lateEntry);
                }
            }

            // Save to disk
            dataStore.saveData();

            JOptionPane.showMessageDialog(this,
                    "Attendance submitted successfully!\n\nPresent: " + presentCount + "\nAbsent: " + absentCount,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        buttonsPanel.add(submitBtn);

        attendancePanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Main layout
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        mainContent.add(selectionPanel, BorderLayout.NORTH);
        mainContent.add(attendancePanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEditAttendancePanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Edit Attendance Records");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Search/Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        filterPanel.add(studentLabel);

        JComboBox<String> studentCombo = new JComboBox<>();
        studentCombo.addItem("All Students");
        for (Student s : dataStore.getAllStudents()) {
            studentCombo.addItem(s.getRollNumber() + " - " + s.getName());
        }
        studentCombo.setPreferredSize(new Dimension(250, 35));
        filterPanel.add(studentCombo);

        JLabel dateFromLabel = new JLabel("From:");
        dateFromLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        filterPanel.add(dateFromLabel);

        JTextField fromField = new JTextField(LocalDate.now().minusDays(7).toString());
        fromField.setPreferredSize(new Dimension(120, 35));
        filterPanel.add(fromField);

        JLabel dateToLabel = new JLabel("To:");
        dateToLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        filterPanel.add(dateToLabel);

        JTextField toField = new JTextField(LocalDate.now().toString());
        toField.setPreferredSize(new Dimension(120, 35));
        filterPanel.add(toField);

        GlowButton searchBtn = new GlowButton("Search", ThemeColors.ACCENT_CYAN);
        filterPanel.add(searchBtn);

        // Attendance records table - load from DataStore
        String[] columns = { "Date", "Student", "Subject", "Period", "Status", "Action" };

        // Use DefaultTableModel for dynamic updates
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Get recent attendance records and store for reference
        final List<Attendance>[] currentRecords = new List[] { new ArrayList<>() };

        // Load initial data
        Runnable loadTableData = () -> {
            tableModel.setRowCount(0);
            currentRecords[0].clear();

            LocalDate fromDate, toDate;
            try {
                fromDate = LocalDate.parse(fromField.getText());
                toDate = LocalDate.parse(toField.getText());
            } catch (Exception ex) {
                fromDate = LocalDate.now().minusDays(7);
                toDate = LocalDate.now();
            }

            String selectedStudent = (String) studentCombo.getSelectedItem();

            List<Attendance> allRecords = dataStore.getAllAttendance();
            final LocalDate finalFromDate = fromDate;
            final LocalDate finalToDate = toDate;

            List<Attendance> filteredRecords = allRecords.stream()
                    .filter(a -> !a.getDate().isBefore(finalFromDate) && !a.getDate().isAfter(finalToDate))
                    .filter(a -> {
                        if ("All Students".equals(selectedStudent))
                            return true;
                        Student student = dataStore.getStudentById(a.getStudentId());
                        if (student == null)
                            return false;
                        String studentStr = student.getRollNumber() + " - " + student.getName();
                        return studentStr.equals(selectedStudent);
                    })
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .limit(50)
                    .toList();

            currentRecords[0] = new ArrayList<>(filteredRecords);

            for (Attendance a : filteredRecords) {
                String studentName = "-";
                for (Student s : dataStore.getAllStudents()) {
                    if (s.getId() == a.getStudentId()) {
                        studentName = s.getName();
                        break;
                    }
                }
                Subject subject = dataStore.getSubjectById(a.getSubjectId());
                tableModel.addRow(new Object[] {
                        a.getDate().toString(),
                        studentName,
                        subject != null ? subject.getName() : "-",
                        String.valueOf(a.getPeriod()),
                        a.getStatus().toString(),
                        "Edit"
                });
            }
        };

        // Load initial data
        loadTableData.run();

        // Search button action
        searchBtn.addActionListener(e -> loadTableData.run());

        JTable table = new JTable(tableModel);
        table.setBackground(ThemeColors.BG_MEDIUM);
        table.setForeground(ThemeColors.TEXT_PRIMARY);
        table.setFont(ThemeColors.FONT_REGULAR);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 50));
        table.setSelectionForeground(ThemeColors.TEXT_PRIMARY);
        table.getTableHeader().setBackground(ThemeColors.BG_LIGHT);
        table.getTableHeader().setForeground(ThemeColors.TEXT_SECONDARY);
        table.getTableHeader().setFont(ThemeColors.FONT_BUTTON);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));

        // Add click handler for Edit button
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 5 && row >= 0 && row < currentRecords[0].size()) { // Edit column
                    Attendance record = currentRecords[0].get(row);
                    showEditAttendanceDialog(record);
                    loadTableData.run(); // Refresh table after edit
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_MEDIUM);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        mainContent.add(filterPanel, BorderLayout.NORTH);
        mainContent.add(tablePanel, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private void showEditAttendanceDialog(Attendance record) {
        Student student = dataStore.getStudentById(record.getStudentId());
        Subject subject = dataStore.getSubjectById(record.getSubjectId());

        String studentName = student != null ? student.getName() : "Unknown";
        String subjectName = subject != null ? subject.getName() : "Unknown";

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.add(new JLabel("Student:"));
        dialogPanel.add(new JLabel(studentName));
        dialogPanel.add(new JLabel("Subject:"));
        dialogPanel.add(new JLabel(subjectName));
        dialogPanel.add(new JLabel("Date:"));
        dialogPanel.add(new JLabel(record.getDate().toString()));
        dialogPanel.add(new JLabel("Period:"));
        dialogPanel.add(new JLabel(String.valueOf(record.getPeriod())));
        dialogPanel.add(new JLabel("Status:"));

        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "PRESENT", "ABSENT", "LATE", "EXCUSED" });
        statusCombo.setSelectedItem(record.getStatus().toString());
        dialogPanel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Edit Attendance Record",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newStatus = (String) statusCombo.getSelectedItem();
            record.setStatus(AttendanceStatus.valueOf(newStatus));
            dataStore.updateAttendance(record);

            JOptionPane.showMessageDialog(this, "Attendance updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the panel
            refreshEditAttendancePanel();
        }
    }

    private void refreshEditAttendancePanel() {
        // Remove old edit attendance panel and create new one
        Component[] components = contentPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            // Find and remove the panel by checking if it's currently shown as
            // EDIT_ATTENDANCE
            // We need to track the panel names, so we'll rebuild by removing and re-adding
        }
        // Remove the existing panel by name
        for (Component comp : contentPanel.getComponents()) {
            contentPanel.remove(comp);
        }
        // Rebuild all panels
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createMarkAttendancePanel(), "MARK_ATTENDANCE");
        contentPanel.add(createEditAttendancePanel(), "EDIT_ATTENDANCE");
        contentPanel.add(createReportsPanel(), "REPORTS");
        contentPanel.add(createClassesPanel(), "CLASSES");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        cardLayout.show(contentPanel, "EDIT_ATTENDANCE");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createReportsPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Class Reports");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Report cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        cardsPanel.add(createReportCard("Class Attendance", "View attendance for your classes", "\uD83D\uDCCA",
                ThemeColors.ACCENT_CYAN));
        cardsPanel.add(
                createReportCard("Defaulters List", "Students below threshold", "\u26A0", ThemeColors.STATUS_WARNING));
        cardsPanel.add(createReportCard("Subject Report", "Subject-wise breakdown", "\uD83D\uDCDA",
                ThemeColors.ACCENT_PURPLE));

        // Defaulters list
        JPanel defaultersPanel = new GradientPanel(true);
        defaultersPanel.setLayout(new BorderLayout());
        defaultersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel defaultersTitle = new JLabel("Defaulters List - Below 75% Attendance");
        defaultersTitle.setFont(ThemeColors.FONT_SUBTITLE);
        defaultersTitle.setForeground(ThemeColors.STATUS_WARNING);
        defaultersPanel.add(defaultersTitle, BorderLayout.NORTH);

        String[] columns = { "Roll No", "Name", "Subject", "Attendance %", "Status" };

        // Get students below threshold from DataStore
        double threshold = dataStore.getAttendanceRule().getMinPercentage();
        List<Object[]> defaulterRows = new ArrayList<>();

        for (Student student : dataStore.getAllStudents()) {
            for (Subject subject : dataStore.getAllSubjects()) {
                double percentage = dataStore.calculateStudentSubjectAttendance(student.getId(), subject.getId());
                if (percentage > 0 && percentage < threshold) {
                    String status = percentage < (threshold - 10) ? "Critical" : "Warning";
                    defaulterRows.add(new Object[] {
                            student.getRollNumber(),
                            student.getName(),
                            subject.getName(),
                            String.format("%.1f%%", percentage),
                            status
                    });
                }
            }
        }

        Object[][] data = defaulterRows.isEmpty()
                ? new Object[][] { { "", "No defaulters found", "", "", "" } }
                : defaulterRows.toArray(new Object[0][]);

        JTable table = createStyledTable(columns, data);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_MEDIUM);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        defaultersPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        mainContent.add(cardsPanel, BorderLayout.NORTH);

        JPanel defaultersContainer = new JPanel(new BorderLayout());
        defaultersContainer.setOpaque(false);
        defaultersContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        defaultersContainer.add(defaultersPanel, BorderLayout.CENTER);
        mainContent.add(defaultersContainer, BorderLayout.CENTER);

        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createClassesPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("My Class Assignments");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Assignments table
        String[] columns = { "Subject", "Section", "Students", "Avg Attendance" };

        List<TeacherSubject> assignments = currentTeacher != null ? dataStore.getTeacherSubjects(currentTeacher.getId())
                : new ArrayList<>();

        Object[][] data = new Object[assignments.size()][4];

        for (int i = 0; i < assignments.size(); i++) {
            TeacherSubject ts = assignments.get(i);
            Subject subject = dataStore.getSubjectById(ts.getSubjectId());
            Section section = dataStore.getSectionById(ts.getSectionId());
            int studentCount = dataStore.getStudentsBySection(ts.getSectionId()).size();

            // Calculate actual average attendance for this subject/section
            double avgAttendance = calculateSectionSubjectAttendance(ts.getSectionId(), ts.getSubjectId());
            data[i] = new Object[] {
                    subject != null ? subject.getName() : "-",
                    section != null ? section.getName() : "-",
                    studentCount,
                    String.format("%.1f%%", avgAttendance)
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

    private JPanel createReportCard(String title, String description, String icon, Color color) {
        JPanel card = new GradientPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(200, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(color);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);

        card.add(Box.createVerticalStrut(10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeColors.FONT_SUBTITLE);
        titleLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(5));

        JLabel descLabel = new JLabel("<html><body style='width: 140px'>" + description + "</body></html>");
        descLabel.setFont(ThemeColors.FONT_SMALL);
        descLabel.setForeground(ThemeColors.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        return card;
    }

    private JTable createStyledTable(String[] columns, Object[][] data) {
        JTable table = new JTable(data, columns);
        table.setBackground(ThemeColors.BG_MEDIUM);
        table.setForeground(ThemeColors.TEXT_PRIMARY);
        table.setFont(ThemeColors.FONT_REGULAR);
        table.setRowHeight(40);
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

    private int getUniqueSubjectsCount(List<TeacherSubject> assignments) {
        return (int) assignments.stream().map(TeacherSubject::getSubjectId).distinct().count();
    }

    private int getUniqueSectionsCount(List<TeacherSubject> assignments) {
        return (int) assignments.stream().map(TeacherSubject::getSectionId).distinct().count();
    }

    /**
     * Calculate average attendance percentage for a subject in a section
     */
    private double calculateSectionSubjectAttendance(int sectionId, int subjectId) {
        List<Student> sectionStudents = dataStore.getStudentsBySection(sectionId);
        if (sectionStudents.isEmpty())
            return 0;

        double totalPercentage = 0;
        int studentCount = 0;
        for (Student student : sectionStudents) {
            double percentage = dataStore.calculateStudentSubjectAttendance(student.getId(), subjectId);
            if (percentage > 0) {
                totalPercentage += percentage;
                studentCount++;
            }
        }
        return studentCount > 0 ? totalPercentage / studentCount : 0;
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
