package attendance.ui.frames;

import attendance.database.DataStore;
import attendance.models.*;
import attendance.services.AuthService;
import attendance.ui.components.*;
import attendance.ui.panels.SettingsPanel;
import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Admin Dashboard with full control panel access
 */
public class AdminDashboard extends JFrame {
    private SidebarPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DataStore dataStore;

    public AdminDashboard() {
        dataStore = DataStore.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - University Attendance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeColors.BG_DARK);

        // Setup sidebar
        sidebar = new SidebarPanel();
        sidebar.setHeader("Admin Panel", "System Administrator");
        sidebar.addItem("\uD83C\uDFE0", "Dashboard", "DASHBOARD");
        sidebar.addItem("\uD83C\uDFDB", "Departments", "DEPARTMENTS");
        sidebar.addItem("\uD83D\uDCDA", "Courses", "COURSES");
        sidebar.addItem("\uD83D\uDD17", "Allotments", "ALLOTMENTS");
        sidebar.addItem("\uD83D\uDC65", "Users", "USERS");
        sidebar.addItem("\u2699", "Attendance Rules", "RULES");
        sidebar.addItem("\uD83D\uDCCA", "Reports", "REPORTS");
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
        contentPanel.add(createDepartmentPanel(), "DEPARTMENTS");
        contentPanel.add(createCoursesPanel(), "COURSES");
        contentPanel.add(createAllotmentsPanel(), "ALLOTMENTS");
        contentPanel.add(createUsersPanel(), "USERS");
        contentPanel.add(createRulesPanel(), "RULES");
        contentPanel.add(createReportsPanel(), "REPORTS");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Dashboard Overview");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Stats cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        cardsPanel.add(new DashboardCard(
                "Total Students",
                String.valueOf(dataStore.getTotalStudents()),
                "\uD83D\uDC68\u200D\uD83C\uDF93",
                ThemeColors.ACCENT_CYAN));

        cardsPanel.add(new DashboardCard(
                "Total Teachers",
                String.valueOf(dataStore.getTotalTeachers()),
                "\uD83D\uDC68\u200D\uD83C\uDFEB",
                ThemeColors.ACCENT_PURPLE));

        cardsPanel.add(new DashboardCard(
                "Departments",
                String.valueOf(dataStore.getTotalDepartments()),
                "\uD83C\uDFDB",
                ThemeColors.ACCENT_BLUE));

        cardsPanel.add(new DashboardCard(
                "Avg Attendance",
                String.format("%.1f%%", dataStore.getOverallAttendancePercentage()),
                "\uD83D\uDCCA",
                ThemeColors.ACCENT_GREEN));

        cardsPanel.add(new DashboardCard(
                "Below Threshold",
                String.valueOf(dataStore.getStudentsBelowThreshold()),
                "\u26A0",
                ThemeColors.STATUS_WARNING));

        // Main content with cards and charts
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        mainContent.add(cardsPanel, BorderLayout.NORTH);

        // Charts panel
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setOpaque(false);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Department attendance chart
        List<Department> departments = dataStore.getAllDepartments();
        String[] deptNames = new String[departments.size()];
        double[] deptValues = new double[departments.size()];

        for (int i = 0; i < departments.size(); i++) {
            deptNames[i] = departments.get(i).getCode();
            deptValues[i] = 75 + Math.random() * 15; // Simulated
        }
        ChartPanel deptChart = new ChartPanel(deptNames, deptValues, ChartPanel.ChartType.BAR, ThemeColors.ACCENT_CYAN);
        chartsPanel.add(deptChart);

        // Monthly trend chart
        String[] months = { "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        double[] monthlyValues = { 78.5, 82.3, 79.1, 85.2, 83.7, dataStore.getOverallAttendancePercentage() };
        ChartPanel trendChart = new ChartPanel(months, monthlyValues, ChartPanel.ChartType.LINE,
                ThemeColors.ACCENT_PURPLE);
        chartsPanel.add(trendChart);

        mainContent.add(chartsPanel, BorderLayout.CENTER);
        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDepartmentPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header with add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Department Management");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        GlowButton addBtn = new GlowButton("+ Add Department", ThemeColors.ACCENT_GREEN);
        addBtn.addActionListener(e -> showAddDepartmentDialog());
        headerPanel.add(addBtn, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Department table
        String[] columns = { "ID", "Name", "Code", "Description", "Actions" };
        List<Department> departments = dataStore.getAllDepartments();
        Object[][] data = new Object[departments.size()][5];

        for (int i = 0; i < departments.size(); i++) {
            Department d = departments.get(i);
            data[i] = new Object[] { d.getId(), d.getName(), d.getCode(), d.getDescription(), "Delete" };
        }

        JTable table = createStyledTable(columns, data);
        // Add delete action
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (col == 4 && row >= 0) { // Actions column
                    int deptId = (int) table.getValueAt(row, 0);
                    deleteDepartment(deptId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(ThemeColors.BG_DARK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void deleteDepartment(int deptId) {
        Department dept = dataStore.getDepartmentById(deptId);
        if (dept == null)
            return;

        // Check if department has courses
        List<Course> courses = dataStore.getCoursesByDepartment(deptId);
        if (!courses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot delete department '" + dept.getName() + "' because it has " + courses.size()
                            + " course(s).\n" +
                            "Please delete all courses in this department first.",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete department '" + dept.getName() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dataStore.deleteDepartment(deptId);

            JOptionPane.showMessageDialog(this,
                    "Department '" + dept.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("DEPARTMENTS");
        }
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Courses, Subjects & Sections");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        GlowButton addCourseBtn = new GlowButton("+ Add Course", ThemeColors.ACCENT_GREEN);
        addCourseBtn.addActionListener(e -> showAddCourseDialog());
        buttonsPanel.add(addCourseBtn);

        GlowButton addSubjectBtn = new GlowButton("+ Add Subject", ThemeColors.ACCENT_PURPLE);
        addSubjectBtn.addActionListener(e -> showAddSubjectDialog());
        buttonsPanel.add(addSubjectBtn);

        GlowButton addSectionBtn = new GlowButton("+ Add Section", ThemeColors.ACCENT_CYAN);
        addSectionBtn.addActionListener(e -> showAddSectionDialog());
        buttonsPanel.add(addSectionBtn);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Courses, subjects, and sections in tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(ThemeColors.BG_MEDIUM);
        tabbedPane.setForeground(ThemeColors.TEXT_PRIMARY);
        tabbedPane.setFont(ThemeColors.FONT_REGULAR);

        // Courses tab with delete
        String[] courseColumns = { "ID", "Department", "Name", "Code", "Duration", "Actions" };
        List<Course> courses = dataStore.getAllCourses();
        Object[][] courseData = new Object[courses.size()][6];

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            Department d = dataStore.getDepartmentById(c.getDepartmentId());
            courseData[i] = new Object[] { c.getId(), d != null ? d.getName() : "-", c.getName(), c.getCode(),
                    c.getDurationYears() + " years", "Delete" };
        }

        JTable courseTable = createStyledTable(courseColumns, courseData);
        courseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = courseTable.columnAtPoint(e.getPoint());
                int row = courseTable.rowAtPoint(e.getPoint());
                if (col == 5 && row >= 0) { // Actions column
                    int courseId = (int) courseTable.getValueAt(row, 0);
                    deleteCourse(courseId);
                }
            }
        });
        tabbedPane.addTab("Courses (" + courses.size() + ")", new JScrollPane(courseTable));

        // Subjects tab with delete
        String[] subjectColumns = { "ID", "Course", "Name", "Code", "Semester", "Credits", "Actions" };
        List<Subject> subjects = dataStore.getAllSubjects();
        Object[][] subjectData = new Object[subjects.size()][7];

        for (int i = 0; i < subjects.size(); i++) {
            Subject s = subjects.get(i);
            Course c = dataStore.getCourseById(s.getCourseId());
            subjectData[i] = new Object[] { s.getId(), c != null ? c.getName() : "-", s.getName(), s.getCode(),
                    s.getSemester(), s.getCredits(), "Delete" };
        }

        JTable subjectTable = createStyledTable(subjectColumns, subjectData);
        subjectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = subjectTable.columnAtPoint(e.getPoint());
                int row = subjectTable.rowAtPoint(e.getPoint());
                if (col == 6 && row >= 0) { // Actions column
                    int subjectId = (int) subjectTable.getValueAt(row, 0);
                    deleteSubject(subjectId);
                }
            }
        });
        tabbedPane.addTab("Subjects (" + subjects.size() + ")", new JScrollPane(subjectTable));

        // Sections tab with delete
        String[] sectionColumns = { "ID", "Course", "Name", "Academic Year", "Semester", "Students", "Actions" };
        List<Section> sections = dataStore.getAllSections();
        Object[][] sectionData = new Object[sections.size()][7];

        for (int i = 0; i < sections.size(); i++) {
            Section sec = sections.get(i);
            Course c = dataStore.getCourseById(sec.getCourseId());
            int studentCount = dataStore.getStudentsBySection(sec.getId()).size();
            sectionData[i] = new Object[] { sec.getId(), c != null ? c.getName() : "-", sec.getName(),
                    sec.getAcademicYear(), sec.getSemester(), studentCount, "Delete" };
        }

        JTable sectionTable = createStyledTable(sectionColumns, sectionData);
        sectionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = sectionTable.columnAtPoint(e.getPoint());
                int row = sectionTable.rowAtPoint(e.getPoint());
                if (col == 6 && row >= 0) { // Actions column
                    int sectionId = (int) sectionTable.getValueAt(row, 0);
                    deleteSection(sectionId);
                }
            }
        });
        tabbedPane.addTab("Sections (" + sections.size() + ")", new JScrollPane(sectionTable));

        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private void showAddSectionDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Course dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        JComboBox<Course> courseCombo = new JComboBox<>();
        for (Course c : dataStore.getAllCourses()) {
            courseCombo.addItem(c);
        }
        panel.add(courseCombo, gbc);

        // Section name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Section Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setToolTipText("e.g., CS-A, CS-B, ECE-1");
        panel.add(nameField, gbc);

        // Academic year
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Academic Year:"), gbc);
        gbc.gridx = 1;
        JTextField yearField = new JTextField("2024-25", 20);
        panel.add(yearField, gbc);

        // Semester
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        panel.add(semesterSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Section", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String year = yearField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Section name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            if (selectedCourse != null) {
                Section newSection = dataStore.addSection(
                        selectedCourse.getId(),
                        name,
                        year,
                        (Integer) semesterSpinner.getValue());

                JOptionPane.showMessageDialog(this,
                        "Section '" + newSection.getName() + "' added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAndShow("COURSES");
            }
        }
    }

    private void deleteSection(int sectionId) {
        Section section = dataStore.getSectionById(sectionId);
        if (section == null)
            return;

        // Check if section has students
        List<Student> students = dataStore.getStudentsBySection(sectionId);
        if (!students.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot delete section '" + section.getName() + "' because it has " + students.size()
                            + " student(s).\n" +
                            "Please move or delete all students in this section first.",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete section '" + section.getName() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dataStore.deleteSection(sectionId);

            JOptionPane.showMessageDialog(this,
                    "Section '" + section.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("COURSES");
        }
    }

    private void deleteCourse(int courseId) {
        Course course = dataStore.getCourseById(courseId);
        if (course == null)
            return;

        // Check if course has subjects
        List<Subject> subjects = dataStore.getSubjectsByCourse(courseId);
        if (!subjects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot delete course '" + course.getName() + "' because it has " + subjects.size()
                            + " subject(s).\n" +
                            "Please delete all subjects in this course first.",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete course '" + course.getName() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dataStore.deleteCourse(courseId);

            JOptionPane.showMessageDialog(this,
                    "Course '" + course.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("COURSES");
        }
    }

    private void deleteSubject(int subjectId) {
        Subject subject = dataStore.getSubjectById(subjectId);
        if (subject == null)
            return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subject '" + subject.getName() + "'?\n" +
                        "This will also delete any related attendance records.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dataStore.deleteSubject(subjectId);

            JOptionPane.showMessageDialog(this,
                    "Subject '" + subject.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("COURSES");
        }
    }

    private JPanel createUsersPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("User Management");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        GlowButton addTeacherBtn = new GlowButton("+ Add Teacher", ThemeColors.ACCENT_CYAN);
        addTeacherBtn.addActionListener(e -> showAddTeacherDialog());
        buttonsPanel.add(addTeacherBtn);

        GlowButton addStudentBtn = new GlowButton("+ Add Student", ThemeColors.ACCENT_PURPLE);
        addStudentBtn.addActionListener(e -> showAddStudentDialog());
        buttonsPanel.add(addStudentBtn);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Users in tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(ThemeColors.BG_MEDIUM);
        tabbedPane.setForeground(ThemeColors.TEXT_PRIMARY);
        tabbedPane.setFont(ThemeColors.FONT_REGULAR);

        // Teachers tab with delete
        String[] teacherColumns = { "ID", "Name", "Username", "Department", "Status", "Actions" };
        List<Teacher> teachers = dataStore.getAllTeachers();
        Object[][] teacherData = new Object[teachers.size()][6];

        for (int i = 0; i < teachers.size(); i++) {
            Teacher t = teachers.get(i);
            User u = dataStore.getUserById(t.getUserId());
            Department d = dataStore.getDepartmentById(t.getDepartmentId());
            teacherData[i] = new Object[] {
                    t.getId(),
                    t.getName(),
                    u != null ? u.getUsername() : "-",
                    d != null ? d.getName() : "-",
                    u != null && u.isActive() ? "Active" : "Inactive",
                    "Delete"
            };
        }

        JTable teacherTable = createStyledTable(teacherColumns, teacherData);
        // Add delete action for teachers
        teacherTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = teacherTable.columnAtPoint(e.getPoint());
                int row = teacherTable.rowAtPoint(e.getPoint());
                if (col == 5 && row >= 0) { // Actions column
                    int teacherId = (int) teacherTable.getValueAt(row, 0);
                    deleteTeacher(teacherId);
                }
            }
        });
        tabbedPane.addTab("Teachers (" + teachers.size() + ")", new JScrollPane(teacherTable));

        // Students tab with delete
        String[] studentColumns = { "ID", "Roll No", "Name", "Username", "Section", "Status", "Actions" };
        List<Student> students = dataStore.getAllStudents();
        Object[][] studentData = new Object[students.size()][7];

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            User u = dataStore.getUserById(s.getUserId());
            Section sec = dataStore.getSectionById(s.getSectionId());
            studentData[i] = new Object[] {
                    s.getId(),
                    s.getRollNumber(),
                    s.getName(),
                    u != null ? u.getUsername() : "-",
                    sec != null ? sec.getName() : "-",
                    u != null && u.isActive() ? "Active" : "Inactive",
                    "Delete"
            };
        }

        JTable studentTable = createStyledTable(studentColumns, studentData);
        // Add delete action for students
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = studentTable.columnAtPoint(e.getPoint());
                int row = studentTable.rowAtPoint(e.getPoint());
                if (col == 6 && row >= 0) { // Actions column
                    int studentId = (int) studentTable.getValueAt(row, 0);
                    deleteStudent(studentId);
                }
            }
        });
        tabbedPane.addTab("Students (" + students.size() + ")", new JScrollPane(studentTable));

        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private void deleteTeacher(int teacherId) {
        Teacher teacher = dataStore.getTeacherById(teacherId);
        if (teacher == null)
            return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete teacher '" + teacher.getName() + "'?\n" +
                        "This will also delete their user account.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Delete teacher and user
            dataStore.deleteTeacher(teacherId);
            dataStore.deleteUser(teacher.getUserId());

            JOptionPane.showMessageDialog(this,
                    "Teacher '" + teacher.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("USERS");
        }
    }

    private void deleteStudent(int studentId) {
        Student student = dataStore.getStudentById(studentId);
        if (student == null)
            return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student '" + student.getName() + "'?\n" +
                        "This will also delete their user account and attendance records.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Delete student and user
            dataStore.deleteStudent(studentId);
            dataStore.deleteUser(student.getUserId());

            JOptionPane.showMessageDialog(this,
                    "Student '" + student.getName() + "' deleted successfully!",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);

            refreshAndShow("USERS");
        }
    }

    private JPanel createAllotmentsPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Subject Allotments");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        GlowButton assignTeacherBtn = new GlowButton("+ Assign Teacher to Subject", ThemeColors.ACCENT_CYAN);
        assignTeacherBtn.addActionListener(e -> showAssignTeacherDialog());
        buttonsPanel.add(assignTeacherBtn);

        GlowButton assignStudentBtn = new GlowButton("+ Assign Student to Subject", ThemeColors.ACCENT_GREEN);
        assignStudentBtn.addActionListener(e -> showAssignStudentToSubjectDialog());
        buttonsPanel.add(assignStudentBtn);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabs for Teacher and Student allotments
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(ThemeColors.BG_MEDIUM);
        tabbedPane.setForeground(ThemeColors.TEXT_PRIMARY);
        tabbedPane.setFont(ThemeColors.FONT_REGULAR);

        // Teacher-Subject tab
        JPanel teacherSubjectPanel = createTeacherSubjectTable();
        tabbedPane.addTab("Teacher-Subject Assignments", teacherSubjectPanel);

        // Student-Subject tab
        JPanel studentSubjectPanel = createStudentSubjectTable();
        tabbedPane.addTab("Student Subject Allotments", studentSubjectPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTeacherSubjectTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        List<TeacherSubject> assignments = dataStore.getAllTeacherSubjects();
        String[] columns = { "Teacher", "Department", "Subject", "Section", "Actions" };
        Object[][] data = new Object[assignments.size()][5];

        for (int i = 0; i < assignments.size(); i++) {
            TeacherSubject ts = assignments.get(i);
            Teacher teacher = dataStore.getTeacherById(ts.getTeacherId());
            Subject subject = dataStore.getSubjectById(ts.getSubjectId());
            Section section = dataStore.getSectionById(ts.getSectionId());
            Department dept = teacher != null ? dataStore.getDepartmentById(teacher.getDepartmentId()) : null;

            data[i] = new Object[] {
                    teacher != null ? teacher.getName() : "-",
                    dept != null ? dept.getName() : "-",
                    subject != null ? subject.getName() + " (" + subject.getCode() + ")" : "-",
                    section != null ? section.getName() : "-",
                    "Remove"
            };
        }

        JTable table = createStyledTable(columns, data);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (col == 4 && row >= 0) { // Actions column
                    removeTeacherSubjectAssignment(row);
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentSubjectTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Get all students with their subjects based on section
        List<Student> students = dataStore.getAllStudents();
        java.util.List<Object[]> rows = new java.util.ArrayList<>();

        for (Student student : students) {
            Section section = dataStore.getSectionById(student.getSectionId());
            if (section != null) {
                Course course = dataStore.getCourseById(section.getCourseId());
                List<Subject> subjects = dataStore.getSubjectsByCourse(section.getCourseId());

                for (Subject subject : subjects) {
                    if (subject.getSemester() == section.getSemester()) {
                        double attendance = dataStore.calculateStudentSubjectAttendance(student.getId(),
                                subject.getId());
                        rows.add(new Object[] {
                                student.getName(),
                                student.getRollNumber(),
                                section.getName(),
                                course != null ? course.getName() : "-",
                                subject.getName(),
                                subject.getCode(),
                                String.format("%.1f%%", attendance)
                        });
                    }
                }
            }
        }

        String[] columns = { "Student", "Roll No", "Section", "Course", "Subject", "Code", "Attendance" };
        Object[][] data = rows.toArray(new Object[0][]);

        JTable table = createStyledTable(columns, data);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel(
                "  Students are automatically assigned subjects based on their Section and current Semester");
        infoLabel.setFont(ThemeColors.FONT_SMALL);
        infoLabel.setForeground(ThemeColors.TEXT_MUTED);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAssignTeacherDialog() {
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Teacher dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogPanel.add(new JLabel("Teacher:"), gbc);
        gbc.gridx = 1;
        JComboBox<Teacher> teacherCombo = new JComboBox<>();
        for (Teacher t : dataStore.getAllTeachers()) {
            teacherCombo.addItem(t);
        }
        dialogPanel.add(teacherCombo, gbc);

        // Subject dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialogPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        JComboBox<Subject> subjectCombo = new JComboBox<>();
        for (Subject s : dataStore.getAllSubjects()) {
            subjectCombo.addItem(s);
        }
        dialogPanel.add(subjectCombo, gbc);

        // Section dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialogPanel.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1;
        JComboBox<Section> sectionCombo = new JComboBox<>();
        for (Section s : dataStore.getAllSections()) {
            sectionCombo.addItem(s);
        }
        dialogPanel.add(sectionCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Assign Teacher to Subject",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Teacher teacher = (Teacher) teacherCombo.getSelectedItem();
            Subject subject = (Subject) subjectCombo.getSelectedItem();
            Section section = (Section) sectionCombo.getSelectedItem();

            if (teacher != null && subject != null && section != null) {
                dataStore.addTeacherSubject(teacher.getId(), subject.getId(), section.getId());

                JOptionPane.showMessageDialog(this,
                        "Assigned " + teacher.getName() + " to teach " + subject.getName() +
                                " in Section " + section.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAndShow("ALLOTMENTS");
            }
        }
    }

    private void removeTeacherSubjectAssignment(int rowIndex) {
        List<TeacherSubject> assignments = dataStore.getAllTeacherSubjects();
        if (rowIndex >= 0 && rowIndex < assignments.size()) {
            TeacherSubject ts = assignments.get(rowIndex);
            Teacher teacher = dataStore.getTeacherById(ts.getTeacherId());
            Subject subject = dataStore.getSubjectById(ts.getSubjectId());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Remove assignment of " + (teacher != null ? teacher.getName() : "Unknown") +
                            " from " + (subject != null ? subject.getName() : "Unknown") + "?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                dataStore.removeTeacherSubject(ts.getTeacherId(), ts.getSubjectId(), ts.getSectionId());

                JOptionPane.showMessageDialog(this,
                        "Assignment removed successfully!",
                        "Removed", JOptionPane.INFORMATION_MESSAGE);
                refreshAndShow("ALLOTMENTS");
            }
        }
    }

    private void showAssignStudentToSubjectDialog() {
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        JComboBox<Student> studentCombo = new JComboBox<>();
        for (Student s : dataStore.getAllStudents()) {
            studentCombo.addItem(s);
        }
        dialogPanel.add(studentCombo, gbc);

        // Subject dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialogPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        JComboBox<Subject> subjectCombo = new JComboBox<>();
        for (Subject s : dataStore.getAllSubjects()) {
            subjectCombo.addItem(s);
        }
        dialogPanel.add(subjectCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Assign Student to Subject",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Student student = (Student) studentCombo.getSelectedItem();
            Subject subject = (Subject) subjectCombo.getSelectedItem();

            if (student != null && subject != null) {
                dataStore.addStudentSubject(student.getId(), subject.getId());

                JOptionPane.showMessageDialog(this,
                        "Assigned " + student.getName() + " to " + subject.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAndShow("ALLOTMENTS");
            }
        }
    }

    private JPanel createRulesPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Attendance Rules Configuration");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Rules form in glass panel
        JPanel formContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formContainer.setOpaque(false);
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JPanel formPanel = new GradientPanel(true);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setPreferredSize(new Dimension(500, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        AttendanceRule rule = dataStore.getAttendanceRule();

        // Min percentage
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel minLabel = new JLabel("Minimum Attendance (%)");
        minLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        minLabel.setFont(ThemeColors.FONT_REGULAR);
        formPanel.add(minLabel, gbc);

        gbc.gridx = 1;
        JSpinner minSpinner = new JSpinner(new SpinnerNumberModel(rule.getMinPercentage(), 0, 100, 1));
        minSpinner.setPreferredSize(new Dimension(100, 35));
        formPanel.add(minSpinner, gbc);

        // Grace percentage
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel graceLabel = new JLabel("Grace Percentage (%)");
        graceLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        graceLabel.setFont(ThemeColors.FONT_REGULAR);
        formPanel.add(graceLabel, gbc);

        gbc.gridx = 1;
        JSpinner graceSpinner = new JSpinner(new SpinnerNumberModel(rule.getGracePercentage(), 0, 20, 1));
        graceSpinner.setPreferredSize(new Dimension(100, 35));
        formPanel.add(graceSpinner, gbc);

        // Detention threshold
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel detentionLabel = new JLabel("Detention Threshold (%)");
        detentionLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        detentionLabel.setFont(ThemeColors.FONT_REGULAR);
        formPanel.add(detentionLabel, gbc);

        gbc.gridx = 1;
        JSpinner detentionSpinner = new JSpinner(new SpinnerNumberModel(rule.getDetentionThreshold(), 0, 100, 1));
        detentionSpinner.setPreferredSize(new Dimension(100, 35));
        formPanel.add(detentionSpinner, gbc);

        // Save button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);

        GlowButton saveBtn = new GlowButton("Save Changes", ThemeColors.ACCENT_GREEN);
        saveBtn.addActionListener(e -> {
            rule.setMinPercentage((Double) minSpinner.getValue());
            rule.setGracePercentage((Double) graceSpinner.getValue());
            rule.setDetentionThreshold((Double) detentionSpinner.getValue());
            dataStore.setAttendanceRule(rule);
            JOptionPane.showMessageDialog(this, "Rules updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        formPanel.add(saveBtn, gbc);

        formContainer.add(formPanel);
        panel.add(formContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Reports & Analytics");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Report options
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Report type cards with real actions
        JPanel card1 = createReportCard("Department Report", "Attendance summary by department", "\uD83C\uDFDB",
                ThemeColors.ACCENT_CYAN, this::generateDepartmentReport);
        JPanel card2 = createReportCard("Subject Report", "Subject-wise attendance analysis", "\uD83D\uDCDA",
                ThemeColors.ACCENT_PURPLE, this::generateSubjectReport);
        JPanel card3 = createReportCard("Detention List", "Students below threshold", "\u26A0",
                ThemeColors.STATUS_WARNING, this::generateDetentionReport);
        JPanel card4 = createReportCard("Export Data", "Download all data as CSV", "\uD83D\uDCBE",
                ThemeColors.ACCENT_GREEN, this::exportAllData);

        optionsPanel.add(card1);
        optionsPanel.add(card2);
        optionsPanel.add(card3);
        optionsPanel.add(card4);

        panel.add(optionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportCard(String title, String description, String icon, Color color, Runnable action) {
        JPanel card = new GradientPanel(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setPreferredSize(new Dimension(220, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setForeground(color);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);

        card.add(Box.createVerticalStrut(15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeColors.FONT_SUBTITLE);
        titleLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(8));

        JLabel descLabel = new JLabel("<html><body style='width: 150px'>" + description + "</body></html>");
        descLabel.setFont(ThemeColors.FONT_SMALL);
        descLabel.setForeground(ThemeColors.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        card.add(Box.createVerticalGlue());

        GlowButton generateBtn = new GlowButton("Generate", color);
        generateBtn.setPreferredSize(new Dimension(170, 35));
        generateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateBtn.addActionListener(e -> action.run());
        card.add(generateBtn);

        return card;
    }

    private void generateDepartmentReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== DEPARTMENT ATTENDANCE REPORT ===\n");
        report.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");

        for (Department dept : dataStore.getAllDepartments()) {
            report.append("Department: ").append(dept.getName()).append(" (").append(dept.getCode()).append(")\n");

            List<Teacher> teachers = dataStore.getTeachersByDepartment(dept.getId());
            report.append("  Teachers: ").append(teachers.size()).append("\n");

            List<Course> courses = dataStore.getCoursesByDepartment(dept.getId());
            int totalStudents = 0;
            for (Course course : courses) {
                for (Section section : dataStore.getSectionsByCourse(course.getId())) {
                    totalStudents += dataStore.getStudentsBySection(section.getId()).size();
                }
            }
            report.append("  Courses: ").append(courses.size()).append("\n");
            report.append("  Students: ").append(totalStudents).append("\n");
            report.append("  ---\n");
        }

        report.append("\nOverall Attendance: ")
                .append(String.format("%.1f%%", dataStore.getOverallAttendancePercentage()));

        showReportDialog("Department Report", report.toString());
    }

    private void generateSubjectReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== SUBJECT-WISE ATTENDANCE REPORT ===\n");
        report.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");

        for (Subject subject : dataStore.getAllSubjects()) {
            Course course = dataStore.getCourseById(subject.getCourseId());
            report.append("Subject: ").append(subject.getName()).append(" (").append(subject.getCode()).append(")\n");
            report.append("  Course: ").append(course != null ? course.getName() : "-").append("\n");
            report.append("  Semester: ").append(subject.getSemester()).append("\n");
            report.append("  Credits: ").append(subject.getCredits()).append("\n");

            List<Attendance> records = dataStore.getAttendanceBySubject(subject.getId());
            if (!records.isEmpty()) {
                long presentCount = records.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.PRESENT
                                || a.getStatus() == AttendanceStatus.LATE)
                        .count();
                double percentage = (presentCount * 100.0) / records.size();
                report.append("  Attendance: ").append(String.format("%.1f%%", percentage)).append(" (")
                        .append(presentCount).append("/").append(records.size()).append(")\n");
            } else {
                report.append("  Attendance: No records\n");
            }
            report.append("  ---\n");
        }

        showReportDialog("Subject Report", report.toString());
    }

    private void generateDetentionReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== DETENTION LIST (Students Below ").append(dataStore.getAttendanceRule().getMinPercentage())
                .append("%) ===\n");
        report.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");

        double threshold = dataStore.getAttendanceRule().getMinPercentage();
        int count = 0;

        for (Student student : dataStore.getAllStudents()) {
            double percentage = dataStore.calculateStudentAttendance(student.getId());
            if (percentage < threshold && percentage > 0) {
                count++;
                Section section = dataStore.getSectionById(student.getSectionId());
                report.append(count).append(". ").append(student.getName()).append("\n");
                report.append("   Roll No: ").append(student.getRollNumber()).append("\n");
                report.append("   Section: ").append(section != null ? section.getName() : "-").append("\n");
                report.append("   Attendance: ").append(String.format("%.1f%%", percentage)).append(" (Below ")
                        .append(threshold).append("%)\n");
                report.append("   ---\n");
            }
        }

        if (count == 0) {
            report.append("No students below the attendance threshold.\n");
        } else {
            report.append("\nTotal Students at Risk: ").append(count);
        }

        showReportDialog("Detention List", report.toString());
    }

    private void exportAllData() {
        try {
            String filename = "attendance_export_" + java.time.LocalDate.now() + ".csv";
            java.io.File file = new java.io.File("reports/" + filename);
            file.getParentFile().mkdirs();

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                // Header
                writer.println("Student Name,Roll No,Section,Subject,Date,Status,Attendance %");

                // Data
                for (Student student : dataStore.getAllStudents()) {
                    Section section = dataStore.getSectionById(student.getSectionId());
                    double overallPercentage = dataStore.calculateStudentAttendance(student.getId());

                    for (Attendance att : dataStore.getAttendanceByStudent(student.getId())) {
                        Subject subject = dataStore.getSubjectById(att.getSubjectId());
                        writer.println(String.format("%s,%s,%s,%s,%s,%s,%.1f%%",
                                student.getName(),
                                student.getRollNumber(),
                                section != null ? section.getName() : "-",
                                subject != null ? subject.getName() : "-",
                                att.getDate(),
                                att.getStatus(),
                                overallPercentage));
                    }
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Data exported successfully!\n\nFile: " + file.getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error exporting data: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReportDialog(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(ThemeColors.BG_DARK);
        textArea.setForeground(ThemeColors.TEXT_PRIMARY);
        textArea.setCaretColor(ThemeColors.TEXT_PRIMARY);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
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

    private void showAddDepartmentDialog() {
        JTextField nameField = new JTextField(20);
        JTextField codeField = new JTextField(10);
        JTextField descField = new JTextField(30);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            dataStore.addDepartment(nameField.getText(), codeField.getText(), descField.getText());
            refreshContent();
        }
    }

    private void showAddCourseDialog() {
        // Create form panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Department dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        JComboBox<Department> deptCombo = new JComboBox<>();
        for (Department d : dataStore.getAllDepartments()) {
            deptCombo.addItem(d);
        }
        deptCombo.setPreferredSize(new Dimension(200, 30));
        panel.add(deptCombo, gbc);

        // Course name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Course Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Course code
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Course Code:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(10);
        panel.add(codeField, gbc);

        // Duration
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Duration (years):"), gbc);

        gbc.gridx = 1;
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 6, 1));
        panel.add(durationSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (nameField.getText().trim().isEmpty() || codeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Department selectedDept = (Department) deptCombo.getSelectedItem();
            if (selectedDept != null) {
                Course newCourse = dataStore.addCourse(
                        selectedDept.getId(),
                        nameField.getText().trim(),
                        codeField.getText().trim().toUpperCase(),
                        (Integer) durationSpinner.getValue());
                JOptionPane.showMessageDialog(this,
                        "Course '" + newCourse.getName() + "' added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshContent();
            }
        }
    }

    private void showAddSubjectDialog() {
        // Create form panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Course dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Course:"), gbc);

        gbc.gridx = 1;
        JComboBox<Course> courseCombo = new JComboBox<>();
        for (Course c : dataStore.getAllCourses()) {
            courseCombo.addItem(c);
        }
        courseCombo.setPreferredSize(new Dimension(200, 30));
        panel.add(courseCombo, gbc);

        // Subject name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Subject Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Subject code
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Subject Code:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(10);
        panel.add(codeField, gbc);

        // Semester
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Semester:"), gbc);

        gbc.gridx = 1;
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        panel.add(semesterSpinner, gbc);

        // Credits
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Credits:"), gbc);

        gbc.gridx = 1;
        JSpinner creditsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 6, 1));
        panel.add(creditsSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Subject",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (nameField.getText().trim().isEmpty() || codeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            if (selectedCourse != null) {
                Subject newSubject = dataStore.addSubject(
                        selectedCourse.getId(),
                        nameField.getText().trim(),
                        codeField.getText().trim().toUpperCase(),
                        (Integer) semesterSpinner.getValue(),
                        (Integer) creditsSpinner.getValue());
                JOptionPane.showMessageDialog(this,
                        "Subject '" + newSubject.getName() + "' added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshContent();
            }
        }
    }

    private void showAddTeacherDialog() {
        // Create form panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Teacher name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Department
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        JComboBox<Department> deptCombo = new JComboBox<>();
        for (Department d : dataStore.getAllDepartments()) {
            deptCombo.addItem(d);
        }
        deptCombo.setPreferredSize(new Dimension(200, 30));
        panel.add(deptCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Teacher",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if username exists
            if (dataStore.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Department selectedDept = (Department) deptCombo.getSelectedItem();
            if (selectedDept != null) {
                // Create user account
                User newUser = dataStore.addUser(username, password, Role.TEACHER, email);
                // Create teacher record
                Teacher newTeacher = dataStore.addTeacher(newUser.getId(), name, selectedDept.getId());

                JOptionPane.showMessageDialog(this,
                        "Teacher '" + name + "' added successfully!\nUsername: " + username,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshContent();
            }
        }
    }

    private void showAddStudentDialog() {
        // Create form panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Roll number
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Roll Number:"), gbc);

        gbc.gridx = 1;
        JTextField rollField = new JTextField(15);
        panel.add(rollField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Section
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Section:"), gbc);

        gbc.gridx = 1;
        JComboBox<Section> sectionCombo = new JComboBox<>();
        for (Section s : dataStore.getAllSections()) {
            sectionCombo.addItem(s);
        }
        sectionCombo.setPreferredSize(new Dimension(200, 30));
        panel.add(sectionCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String rollNo = rollField.getText().trim().toUpperCase();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || rollNo.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if username exists
            if (dataStore.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Section selectedSection = (Section) sectionCombo.getSelectedItem();
            if (selectedSection != null) {
                // Create user account
                User newUser = dataStore.addUser(username, password, Role.STUDENT, email);
                // Create student record
                Student newStudent = dataStore.addStudent(newUser.getId(), selectedSection.getId(), rollNo, name);

                JOptionPane.showMessageDialog(this,
                        "Student '" + name + "' added successfully!\nRoll No: " + rollNo + "\nUsername: " + username,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshContent();
            }
        }
    }

    private void refreshContent() {
        // Get current panel name
        String currentPanel = "DASHBOARD";

        // Remove all panels and rebuild them with fresh data
        contentPanel.removeAll();

        // Rebuild all panels with current data
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createDepartmentPanel(), "DEPARTMENTS");
        contentPanel.add(createCoursesPanel(), "COURSES");
        contentPanel.add(createAllotmentsPanel(), "ALLOTMENTS");
        contentPanel.add(createUsersPanel(), "USERS");
        contentPanel.add(createRulesPanel(), "RULES");
        contentPanel.add(createReportsPanel(), "REPORTS");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        // Show current panel
        cardLayout.show(contentPanel, currentPanel);

        // Force repaint
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshAndShow(String panelName) {
        // Remove all panels and rebuild them with fresh data
        contentPanel.removeAll();

        // Rebuild all panels with current data
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createDepartmentPanel(), "DEPARTMENTS");
        contentPanel.add(createCoursesPanel(), "COURSES");
        contentPanel.add(createAllotmentsPanel(), "ALLOTMENTS");
        contentPanel.add(createUsersPanel(), "USERS");
        contentPanel.add(createRulesPanel(), "RULES");
        contentPanel.add(createReportsPanel(), "REPORTS");
        contentPanel.add(new SettingsPanel(this), "SETTINGS");

        // Show requested panel
        cardLayout.show(contentPanel, panelName);

        // Force repaint
        contentPanel.revalidate();
        contentPanel.repaint();
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
