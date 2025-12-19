package attendance.models;

import java.io.Serializable;

/**
 * Student-Subject assignment (manual subject assignment for students)
 */
public class StudentSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    private int studentId;
    private int subjectId;

    public StudentSubject(int studentId, int subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
