package attendance.models;

import java.io.Serializable;

/**
 * Section entity representing a class section
 */
public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int courseId;
    private String name;
    private String academicYear;
    private int semester;

    public Section(int id, int courseId, String name, String academicYear, int semester) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.academicYear = academicYear;
        this.semester = semester;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return name + " (" + academicYear + " - Sem " + semester + ")";
    }
}
