package attendance.models;

import java.io.Serializable;

/**
 * Student entity
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private int sectionId;
    private String rollNumber;
    private String name;

    public Student(int id, int userId, int sectionId, String rollNumber, String name) {
        this.id = id;
        this.userId = userId;
        this.sectionId = sectionId;
        this.rollNumber = rollNumber;
        this.name = name;
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return rollNumber + " - " + name;
    }
}
