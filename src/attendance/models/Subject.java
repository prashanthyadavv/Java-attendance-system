package attendance.models;

import java.io.Serializable;

/**
 * Subject entity
 */
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int courseId;
    private String name;
    private String code;
    private int semester;
    private int credits;

    public Subject(int id, int courseId, String name, String code, int semester, int credits) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.code = code;
        this.semester = semester;
        this.credits = credits;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
