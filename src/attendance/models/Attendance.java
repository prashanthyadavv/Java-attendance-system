package attendance.models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Attendance record entity
 */
public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int studentId;
    private int subjectId;
    private LocalDate date;
    private int period;
    private AttendanceStatus status;
    private int markedBy;
    private boolean lateEntry;
    private String editReason;

    public Attendance(int id, int studentId, int subjectId, LocalDate date, int period,
            AttendanceStatus status, int markedBy, boolean lateEntry) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.period = period;
        this.status = status;
        this.markedBy = markedBy;
        this.lateEntry = lateEntry;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public int getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(int markedBy) {
        this.markedBy = markedBy;
    }

    public boolean isLateEntry() {
        return lateEntry;
    }

    public void setLateEntry(boolean lateEntry) {
        this.lateEntry = lateEntry;
    }

    public String getEditReason() {
        return editReason;
    }

    public void setEditReason(String editReason) {
        this.editReason = editReason;
    }
}
