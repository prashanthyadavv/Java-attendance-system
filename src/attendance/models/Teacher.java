package attendance.models;

import java.io.Serializable;

/**
 * Teacher entity
 */
public class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private String name;
    private int departmentId;

    public Teacher(int id, int userId, String name, int departmentId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.departmentId = departmentId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return name;
    }
}
