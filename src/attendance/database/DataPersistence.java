package attendance.database;

import attendance.models.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Handles saving and loading data to/from JSON files
 * Data is stored in 'data/' folder
 */
public class DataPersistence {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + "/attendance_data.dat";

    /**
     * Save all data to file
     */
    public static void saveData(DataStore store) {
        try {
            // Create data directory if not exists
            Files.createDirectories(Paths.get(DATA_DIR));

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(DATA_FILE))) {

                // Save all collections
                oos.writeObject(store.getAllUsers());
                oos.writeObject(store.getAllDepartments());
                oos.writeObject(store.getAllCourses());
                oos.writeObject(store.getAllSubjects());
                oos.writeObject(store.getAllSections());
                oos.writeObject(store.getAllStudents());
                oos.writeObject(store.getAllTeachers());
                oos.writeObject(store.getAllAttendance());
                oos.writeObject(store.getAllTeacherSubjects());
                oos.writeObject(store.getAttendanceRule());

                // Save ID counters
                oos.writeObject(store.getNextIds());

                System.out.println("[DataPersistence] Data saved successfully to " + DATA_FILE);
            }
        } catch (IOException e) {
            System.err.println("[DataPersistence] Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load data from file
     * Returns true if data was loaded, false if file doesn't exist
     */
    @SuppressWarnings("unchecked")
    public static boolean loadData(DataStore store) {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("[DataPersistence] No saved data found, using defaults");
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {

            // Load all collections
            List<User> users = (List<User>) ois.readObject();
            List<Department> departments = (List<Department>) ois.readObject();
            List<Course> courses = (List<Course>) ois.readObject();
            List<Subject> subjects = (List<Subject>) ois.readObject();
            List<Section> sections = (List<Section>) ois.readObject();
            List<Student> students = (List<Student>) ois.readObject();
            List<Teacher> teachers = (List<Teacher>) ois.readObject();
            List<Attendance> attendance = (List<Attendance>) ois.readObject();
            List<TeacherSubject> teacherSubjects = (List<TeacherSubject>) ois.readObject();
            AttendanceRule rule = (AttendanceRule) ois.readObject();

            // Load ID counters
            int[] nextIds = (int[]) ois.readObject();

            // Set data in store
            store.setLoadedData(users, departments, courses, subjects, sections,
                    students, teachers, attendance, teacherSubjects, rule, nextIds);

            System.out.println("[DataPersistence] Data loaded successfully from " + DATA_FILE);
            System.out.println("  - Users: " + users.size());
            System.out.println("  - Teachers: " + teachers.size());
            System.out.println("  - Students: " + students.size());
            System.out.println("  - Attendance records: " + attendance.size());
            return true;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DataPersistence] Error loading data: " + e.getMessage());
            System.out.println("[DataPersistence] Using default sample data instead");
            return false;
        }
    }

    /**
     * Delete saved data file
     */
    public static void clearData() {
        try {
            Files.deleteIfExists(Paths.get(DATA_FILE));
            System.out.println("[DataPersistence] Data file deleted");
        } catch (IOException e) {
            System.err.println("[DataPersistence] Error deleting data: " + e.getMessage());
        }
    }

    /**
     * Check if saved data exists
     */
    public static boolean hasSavedData() {
        return new File(DATA_FILE).exists();
    }
}
