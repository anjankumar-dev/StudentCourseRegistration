package org.anjan.service;

import org.anjan.dao.EnrollmentDAO;

import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO dao = new EnrollmentDAO();

    public void enroll(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) throw new IllegalArgumentException("Student ID and course ID must be positive.");
        dao.enroll(studentId, courseId);
    }
    public List<String> studentsInCourse(int courseId) throws SQLException {
        if (courseId <= 0) throw new IllegalArgumentException("Course ID must be positive.");
        return dao.studentsInCourse(courseId);
    }
    public List<String> coursesForStudent(int studentId) throws SQLException {
        if (studentId <= 0) throw new IllegalArgumentException("Student ID must be positive.");
        return dao.coursesForStudent(studentId);
    }
    public List<String> report() throws SQLException { return dao.studentCourseReport(); }
    public List<String> storedProcedure(int studentId) throws SQLException {
        if (studentId <= 0) throw new IllegalArgumentException("Student ID must be positive.");
        return dao.callStudentCoursesProcedure(studentId);
    }
    public int batchEnroll(int studentId, List<Integer> courseIds) {
        if (studentId <= 0) throw new IllegalArgumentException("Student ID must be positive.");
        if (courseIds == null || courseIds.isEmpty()) throw new IllegalArgumentException("Enter at least one course ID.");
        if (courseIds.stream().anyMatch(id -> id == null || id <= 0)) throw new IllegalArgumentException("All course IDs must be positive.");
        return dao.batchEnroll(studentId, courseIds);
    }
}
