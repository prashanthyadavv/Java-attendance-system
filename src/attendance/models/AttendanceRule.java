package attendance.models;

import java.io.Serializable;

/**
 * Attendance rules configuration
 */
public class AttendanceRule implements Serializable {
    private static final long serialVersionUID = 1L;
    private double minPercentage;
    private double gracePercentage;
    private double detentionThreshold;

    public AttendanceRule() {
        // Default values
        this.minPercentage = 75.0;
        this.gracePercentage = 5.0;
        this.detentionThreshold = 65.0;
    }

    public AttendanceRule(double minPercentage, double gracePercentage, double detentionThreshold) {
        this.minPercentage = minPercentage;
        this.gracePercentage = gracePercentage;
        this.detentionThreshold = detentionThreshold;
    }

    // Getters and Setters
    public double getMinPercentage() {
        return minPercentage;
    }

    public void setMinPercentage(double minPercentage) {
        this.minPercentage = minPercentage;
    }

    public double getGracePercentage() {
        return gracePercentage;
    }

    public void setGracePercentage(double gracePercentage) {
        this.gracePercentage = gracePercentage;
    }

    public double getDetentionThreshold() {
        return detentionThreshold;
    }

    public void setDetentionThreshold(double detentionThreshold) {
        this.detentionThreshold = detentionThreshold;
    }

    /**
     * Get the warning threshold (min - grace)
     */
    public double getWarningThreshold() {
        return minPercentage - gracePercentage;
    }
}
