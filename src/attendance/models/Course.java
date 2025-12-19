package attendance.models;

import java.io.Serializable;

/**
 * Course entity
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int departmentId;
    private String name;
    private String code;
    private int durationYears;

    public Course(int id, int departmentId, String name, String code, int durationYears) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
        this.durationYears = durationYears;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
