package attendance.database;

import attendance.models.*;
import java.time.LocalDate;
import java.util.*;

/**
 * In-memory data store with file persistence
 * Data is automatically saved to disk and loaded on startup
 */
public class DataStore {
    private static DataStore instance;

    // Data collections
    private Map<Integer, User> users = new HashMap<>();
    private Map<Integer, Department> departments = new HashMap<>();
    private Map<Integer, Course> courses = new HashMap<>();
    private Map<Integer, Subject> subjects = new HashMap<>();
    private Map<Integer, Section> sections = new HashMap<>();
    private Map<Integer, Student> students = new HashMap<>();
    private Map<Integer, Teacher> teachers = new HashMap<>();
    private List<Attendance> attendanceRecords = new ArrayList<>();
    private List<TeacherSubject> teacherSubjects = new ArrayList<>();
    private List<StudentSubject> studentSubjects = new ArrayList<>();
    private AttendanceRule attendanceRule = new AttendanceRule();

    // ID counters
    private int nextUserId = 1;
    private int nextDepartmentId = 1;
    private int nextCourseId = 1;
    private int nextSubjectId = 1;
    private int nextSectionId = 1;
    private int nextStudentId = 1;
    private int nextTeacherId = 1;
    private int nextAttendanceId = 1;

    // Current logged-in user
    private User currentUser;

    private DataStore() {
        // Try to load saved data first
        boolean loaded = DataPersistence.loadData(this);
        if (!loaded) {
            // No saved data, initialize with sample data
            initializeSampleData();
            saveData(); // Save the sample data
        }
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Save current data to disk
     */
    public void saveData() {
        DataPersistence.saveData(this);
    }

    /**
     * Get ID counters for persistence
     */
    public int[] getNextIds() {
        return new int[] { nextUserId, nextDepartmentId, nextCourseId, nextSubjectId,
                nextSectionId, nextStudentId, nextTeacherId, nextAttendanceId };
    }

    /**
     * Load data from persistence
     */
    public void setLoadedData(List<User> userList, List<Department> deptList,
            List<Course> courseList, List<Subject> subjectList, List<Section> sectionList,
            List<Student> studentList, List<Teacher> teacherList,
            List<Attendance> attendanceList, List<TeacherSubject> teacherSubjectList,
            AttendanceRule rule, int[] nextIds) {

        // Clear existing data
        users.clear();
        departments.clear();
        courses.clear();
        subjects.clear();
        sections.clear();
        students.clear();
        teachers.clear();
        attendanceRecords.clear();
        teacherSubjects.clear();

        // Load collections into maps
        for (User u : userList)
            users.put(u.getId(), u);
        for (Department d : deptList)
            departments.put(d.getId(), d);
        for (Course c : courseList)
            courses.put(c.getId(), c);
        for (Subject s : subjectList)
            subjects.put(s.getId(), s);
        for (Section s : sectionList)
            sections.put(s.getId(), s);
        for (Student s : studentList)
            students.put(s.getId(), s);
        for (Teacher t : teacherList)
            teachers.put(t.getId(), t);

        attendanceRecords.addAll(attendanceList);
        teacherSubjects.addAll(teacherSubjectList);
        attendanceRule = rule != null ? rule : new AttendanceRule();

        // Restore ID counters
        if (nextIds != null && nextIds.length == 8) {
            nextUserId = nextIds[0];
            nextDepartmentId = nextIds[1];
            nextCourseId = nextIds[2];
            nextSubjectId = nextIds[3];
            nextSectionId = nextIds[4];
            nextStudentId = nextIds[5];
            nextTeacherId = nextIds[6];
            nextAttendanceId = nextIds[7];
        }
    }

    /**
     * Initialize sample data for testing
     */
    private void initializeSampleData() {
        // Create departments
        Department csDept = addDepartment("Computer Science", "CS", "Department of Computer Science and Engineering");
        Department eceDept = addDepartment("Electronics", "ECE", "Department of Electronics and Communication");
        Department mechDept = addDepartment("Mechanical", "MECH", "Department of Mechanical Engineering");

        // Create courses
        Course btech_cs = addCourse(csDept.getId(), "B.Tech Computer Science", "BTCS", 4);
        Course btech_ece = addCourse(eceDept.getId(), "B.Tech Electronics", "BTECE", 4);
        Course btech_mech = addCourse(mechDept.getId(), "B.Tech Mechanical", "BTMECH", 4);

        // Create subjects for CS
        Subject java = addSubject(btech_cs.getId(), "Java Programming", "CS201", 3, 4);
        Subject dbms = addSubject(btech_cs.getId(), "Database Management", "CS202", 3, 4);
        Subject os = addSubject(btech_cs.getId(), "Operating Systems", "CS203", 3, 3);
        Subject cn = addSubject(btech_cs.getId(), "Computer Networks", "CS204", 4, 4);
        Subject dsa = addSubject(btech_cs.getId(), "Data Structures", "CS205", 3, 4);

        // Create subjects for ECE
        Subject signals = addSubject(btech_ece.getId(), "Signals & Systems", "EC201", 3, 4);
        Subject vlsi = addSubject(btech_ece.getId(), "VLSI Design", "EC202", 4, 4);

        // Create sections
        Section csA = addSection(btech_cs.getId(), "CS-A", "2024-25", 3);
        Section csB = addSection(btech_cs.getId(), "CS-B", "2024-25", 3);
        Section eceA = addSection(btech_ece.getId(), "ECE-A", "2024-25", 3);

        // Create admin user
        addUser("admin", "admin123", Role.ADMIN, "admin@university.edu");

        // Create teachers
        User t1User = addUser("john.smith", "teacher123", Role.TEACHER, "john.smith@university.edu");
        Teacher t1 = addTeacher(t1User.getId(), "Dr. John Smith", csDept.getId());

        User t2User = addUser("sarah.wilson", "teacher123", Role.TEACHER, "sarah.wilson@university.edu");
        Teacher t2 = addTeacher(t2User.getId(), "Prof. Sarah Wilson", csDept.getId());

        User t3User = addUser("mike.brown", "teacher123", Role.TEACHER, "mike.brown@university.edu");
        Teacher t3 = addTeacher(t3User.getId(), "Dr. Mike Brown", eceDept.getId());

        // Assign teachers to subjects
        addTeacherSubject(t1.getId(), java.getId(), csA.getId());
        addTeacherSubject(t1.getId(), java.getId(), csB.getId());
        addTeacherSubject(t1.getId(), dsa.getId(), csA.getId());
        addTeacherSubject(t2.getId(), dbms.getId(), csA.getId());
        addTeacherSubject(t2.getId(), os.getId(), csA.getId());
        addTeacherSubject(t3.getId(), signals.getId(), eceA.getId());

        // Create students for CS-A
        String[] studentNames = {
                "Alice Johnson", "Bob Williams", "Charlie Davis", "Diana Miller",
                "Edward Brown", "Fiona Taylor", "George Anderson", "Hannah Thomas",
                "Ian Jackson", "Julia White"
        };

        List<Student> csaStudents = new ArrayList<>();
        for (int i = 0; i < studentNames.length; i++) {
            String username = studentNames[i].toLowerCase().replace(" ", ".");
            User sUser = addUser(username, "student123", Role.STUDENT, username + "@student.university.edu");
            Student student = addStudent(sUser.getId(), csA.getId(), "CS" + String.format("%03d", i + 1),
                    studentNames[i]);
            csaStudents.add(student);
        }

        // No sample attendance - users will add their own
    }

    // === User operations ===
    public User addUser(String username, String password, Role role, String email) {
        User user = new User(nextUserId++, username, password, role, email);
        users.put(user.getId(), user);
        saveData();
        return user;
    }

    public User getUserByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<User> getUsersByRole(Role role) {
        return users.values().stream()
                .filter(u -> u.getRole() == role)
                .toList();
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
        saveData();
    }

    public void deleteUser(int id) {
        users.remove(id);
        saveData();
    }

    // === Department operations ===
    public Department addDepartment(String name, String code, String description) {
        Department dept = new Department(nextDepartmentId++, name, code, description);
        departments.put(dept.getId(), dept);
        saveData();
        return dept;
    }

    public Department getDepartmentById(int id) {
        return departments.get(id);
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments.values());
    }

    public void updateDepartment(Department dept) {
        departments.put(dept.getId(), dept);
        saveData();
    }

    public void deleteDepartment(int id) {
        departments.remove(id);
        saveData();
    }

    // === Course operations ===
    public Course addCourse(int departmentId, String name, String code, int durationYears) {
        Course course = new Course(nextCourseId++, departmentId, name, code, durationYears);
        courses.put(course.getId(), course);
        saveData();
        return course;
    }

    public Course getCourseById(int id) {
        return courses.get(id);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<Course> getCoursesByDepartment(int departmentId) {
        return courses.values().stream()
                .filter(c -> c.getDepartmentId() == departmentId)
                .toList();
    }

    public void updateCourse(Course course) {
        courses.put(course.getId(), course);
        saveData();
    }

    public void deleteCourse(int id) {
        courses.remove(id);
        saveData();
    }

    // === Subject operations ===
    public Subject addSubject(int courseId, String name, String code, int semester, int credits) {
        Subject subject = new Subject(nextSubjectId++, courseId, name, code, semester, credits);
        subjects.put(subject.getId(), subject);
        saveData();
        return subject;
    }

    public Subject getSubjectById(int id) {
        return subjects.get(id);
    }

    public List<Subject> getAllSubjects() {
        return new ArrayList<>(subjects.values());
    }

    public List<Subject> getSubjectsByCourse(int courseId) {
        return subjects.values().stream()
                .filter(s -> s.getCourseId() == courseId)
                .toList();
    }

    public void updateSubject(Subject subject) {
        subjects.put(subject.getId(), subject);
        saveData();
    }

    public void deleteSubject(int id) {
        subjects.remove(id);
        saveData();
    }

    // === Section operations ===
    public Section addSection(int courseId, String name, String academicYear, int semester) {
        Section section = new Section(nextSectionId++, courseId, name, academicYear, semester);
        sections.put(section.getId(), section);
        saveData();
        return section;
    }

    public Section getSectionById(int id) {
        return sections.get(id);
    }

    public List<Section> getAllSections() {
        return new ArrayList<>(sections.values());
    }

    public List<Section> getSectionsByCourse(int courseId) {
        return sections.values().stream()
                .filter(s -> s.getCourseId() == courseId)
                .toList();
    }

    public void updateSection(Section section) {
        sections.put(section.getId(), section);
        saveData();
    }

    public void deleteSection(int id) {
        sections.remove(id);
        saveData();
    }

    // === Student operations ===
    public Student addStudent(int userId, int sectionId, String rollNumber, String name) {
        Student student = new Student(nextStudentId++, userId, sectionId, rollNumber, name);
        students.put(student.getId(), student);
        saveData();
        return student;
    }

    public Student getStudentById(int id) {
        return students.get(id);
    }

    public Student getStudentByUserId(int userId) {
        return students.values().stream()
                .filter(s -> s.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Student> getStudentsBySection(int sectionId) {
        return students.values().stream()
                .filter(s -> s.getSectionId() == sectionId)
                .toList();
    }

    public void updateStudent(Student student) {
        students.put(student.getId(), student);
        saveData();
    }

    public void deleteStudent(int id) {
        students.remove(id);
        saveData();
    }

    // === Teacher operations ===
    public Teacher addTeacher(int userId, String name, int departmentId) {
        Teacher teacher = new Teacher(nextTeacherId++, userId, name, departmentId);
        teachers.put(teacher.getId(), teacher);
        saveData();
        return teacher;
    }

    public Teacher getTeacherById(int id) {
        return teachers.get(id);
    }

    public Teacher getTeacherByUserId(int userId) {
        return teachers.values().stream()
                .filter(t -> t.getUserId() == userId)
                .findFirst()
                .orElse(null);
    }

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers.values());
    }

    public List<Teacher> getTeachersByDepartment(int departmentId) {
        return teachers.values().stream()
                .filter(t -> t.getDepartmentId() == departmentId)
                .toList();
    }

    public void updateTeacher(Teacher teacher) {
        teachers.put(teacher.getId(), teacher);
        saveData();
    }

    public void deleteTeacher(int id) {
        teachers.remove(id);
        saveData();
    }

    // === Teacher-Subject operations ===
    public void addTeacherSubject(int teacherId, int subjectId, int sectionId) {
        teacherSubjects.add(new TeacherSubject(teacherId, subjectId, sectionId));
        saveData();
    }

    public List<TeacherSubject> getTeacherSubjects(int teacherId) {
        return teacherSubjects.stream()
                .filter(ts -> ts.getTeacherId() == teacherId)
                .toList();
    }

    public List<TeacherSubject> getAllTeacherSubjects() {
        return new ArrayList<>(teacherSubjects);
    }

    public void removeTeacherSubject(int teacherId, int subjectId, int sectionId) {
        teacherSubjects.removeIf(ts -> ts.getTeacherId() == teacherId &&
                ts.getSubjectId() == subjectId &&
                ts.getSectionId() == sectionId);
        saveData();
    }

    // === Student-Subject operations ===
    public void addStudentSubject(int studentId, int subjectId) {
        // Check if already exists
        boolean exists = studentSubjects.stream()
                .anyMatch(ss -> ss.getStudentId() == studentId && ss.getSubjectId() == subjectId);
        if (!exists) {
            studentSubjects.add(new StudentSubject(studentId, subjectId));
            saveData();
        }
    }

    public List<StudentSubject> getAllStudentSubjects() {
        return new ArrayList<>(studentSubjects);
    }

    public List<StudentSubject> getStudentSubjectsByStudent(int studentId) {
        return studentSubjects.stream()
                .filter(ss -> ss.getStudentId() == studentId)
                .toList();
    }

    public void removeStudentSubject(int studentId, int subjectId) {
        studentSubjects.removeIf(ss -> ss.getStudentId() == studentId && ss.getSubjectId() == subjectId);
        saveData();
    }

    // === Attendance operations ===
    public Attendance addAttendance(int studentId, int subjectId, LocalDate date, int period,
            AttendanceStatus status, int markedBy, boolean lateEntry) {
        // Check for duplicate
        boolean exists = attendanceRecords.stream()
                .anyMatch(a -> a.getStudentId() == studentId &&
                        a.getSubjectId() == subjectId &&
                        a.getDate().equals(date) &&
                        a.getPeriod() == period);

        if (exists) {
            // Update existing record instead
            attendanceRecords.stream()
                    .filter(a -> a.getStudentId() == studentId &&
                            a.getSubjectId() == subjectId &&
                            a.getDate().equals(date) &&
                            a.getPeriod() == period)
                    .findFirst()
                    .ifPresent(a -> {
                        a.setStatus(status);
                        a.setMarkedBy(markedBy);
                        a.setLateEntry(lateEntry);
                    });
            return null;
        }

        Attendance attendance = new Attendance(nextAttendanceId++, studentId, subjectId, date, period, status, markedBy,
                lateEntry);
        attendanceRecords.add(attendance);
        return attendance;
    }

    public List<Attendance> getAttendanceByStudent(int studentId) {
        return attendanceRecords.stream()
                .filter(a -> a.getStudentId() == studentId)
                .toList();
    }

    public List<Attendance> getAttendanceBySubject(int subjectId) {
        return attendanceRecords.stream()
                .filter(a -> a.getSubjectId() == subjectId)
                .toList();
    }

    public List<Attendance> getAttendanceByStudentAndSubject(int studentId, int subjectId) {
        return attendanceRecords.stream()
                .filter(a -> a.getStudentId() == studentId && a.getSubjectId() == subjectId)
                .toList();
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRecords.stream()
                .filter(a -> a.getDate().equals(date))
                .toList();
    }

    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRecords.stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .toList();
    }

    public List<Attendance> getAllAttendance() {
        return new ArrayList<>(attendanceRecords);
    }

    public void updateAttendance(Attendance attendance) {
        for (int i = 0; i < attendanceRecords.size(); i++) {
            if (attendanceRecords.get(i).getId() == attendance.getId()) {
                attendanceRecords.set(i, attendance);
                break;
            }
        }
    }

    // === Attendance Rules ===
    public AttendanceRule getAttendanceRule() {
        return attendanceRule;
    }

    public void setAttendanceRule(AttendanceRule rule) {
        this.attendanceRule = rule;
    }

    // === Session management ===
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    // === Statistics ===
    public int getTotalStudents() {
        return students.size();
    }

    public int getTotalTeachers() {
        return teachers.size();
    }

    public int getTotalDepartments() {
        return departments.size();
    }

    public double getOverallAttendancePercentage() {
        if (attendanceRecords.isEmpty())
            return 0;

        long presentCount = attendanceRecords.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();

        return (presentCount * 100.0) / attendanceRecords.size();
    }

    public int getStudentsBelowThreshold() {
        double threshold = attendanceRule.getMinPercentage();
        int count = 0;

        for (Student student : students.values()) {
            double percentage = calculateStudentAttendance(student.getId());
            if (percentage < threshold && percentage > 0) {
                count++;
            }
        }

        return count;
    }

    public double calculateStudentAttendance(int studentId) {
        List<Attendance> studentRecords = getAttendanceByStudent(studentId);
        if (studentRecords.isEmpty())
            return 0;

        long presentCount = studentRecords.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();

        return (presentCount * 100.0) / studentRecords.size();
    }

    public double calculateStudentSubjectAttendance(int studentId, int subjectId) {
        List<Attendance> records = getAttendanceByStudentAndSubject(studentId, subjectId);
        if (records.isEmpty())
            return 0;

        long presentCount = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();

        return (presentCount * 100.0) / records.size();
    }
}
