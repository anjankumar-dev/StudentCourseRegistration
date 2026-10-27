package org.anjan.model;

import java.math.BigDecimal;

public class Course {
    private int courseId;
    private String courseName;
    private int durationMonths;
    private BigDecimal fee;

    public Course() {}
    public Course(int courseId, String courseName, int durationMonths, BigDecimal fee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.durationMonths = durationMonths;
        this.fee = fee;
    }
    public Course(String courseName, int durationMonths, BigDecimal fee) {
        this(0, courseName, durationMonths, fee);
    }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    @Override public String toString() {
        return "Course{ID=" + courseId + ", name='" + courseName +
                "', durationMonths=" + durationMonths + ", fee=" + fee + "}";
    }
}
