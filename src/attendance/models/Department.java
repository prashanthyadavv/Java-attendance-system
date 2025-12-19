package attendance.models;

import java.io.Serializable;

/**
 * Department entity
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String code;
    private String description;

    public Department(int id, String name, String code, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
