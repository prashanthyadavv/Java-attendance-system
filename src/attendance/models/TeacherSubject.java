package attendance.models;

import java.io.Serializable;

/**
 * Teacher-Subject assignment (many-to-many relationship)
 */
public class TeacherSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    private int teacherId;
    private int subjectId;
    private int sectionId;

    public TeacherSubject(int teacherId, int subjectId, int sectionId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.sectionId = sectionId;
    }

    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }
}
