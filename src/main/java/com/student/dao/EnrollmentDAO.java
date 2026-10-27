package com.student.dao;

import com.student.config.DBConnection;
import com.student.exception.EnrollmentException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    public void enroll(int studentId, int courseId) {
        String sql = "INSERT INTO enrollments(student_id, course_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                try { con.rollback(); } catch (SQLException rollbackError) { e.addSuppressed(rollbackError); }
                if ("23000".equals(e.getSQLState())) {
                    throw new EnrollmentException(
                            "Enrollment failed: student/course ID may not exist, or this student is already enrolled.", e);
                }
                throw new EnrollmentException("Enrollment failed; transaction rolled back.", e);
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException ignored) { }
            }
        } catch (SQLException e) {
            throw new EnrollmentException("Could not get a database connection for enrollment.", e);
        }
    }

    public List<String> studentsInCourse(int courseId) throws SQLException {
        String sql = """
                SELECT s.student_id, s.name, s.email, e.enrollment_date
                FROM enrollments e
                JOIN students s ON s.student_id = e.student_id
                WHERE e.course_id = ?
                ORDER BY s.name
                """;
        List<String> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add("Student ID: " + rs.getInt("student_id") +
                        " | Name: " + rs.getString("name") + " | Email: " +
                        rs.getString("email") + " | Enrolled: " + rs.getDate("enrollment_date"));
            }
        }
        return rows;
    }

    public List<String> coursesForStudent(int studentId) throws SQLException {
        String sql = """
                SELECT c.course_id, c.course_name, c.duration_months, c.fee, e.enrollment_date
                FROM enrollments e
                JOIN courses c ON c.course_id = e.course_id
                WHERE e.student_id = ?
                ORDER BY c.course_name
                """;
        List<String> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add("Course ID: " + rs.getInt("course_id") +
                        " | " + rs.getString("course_name") + " | Duration: " +
                        rs.getInt("duration_months") + " months | Fee: " +
                        rs.getBigDecimal("fee") + " | Enrolled: " + rs.getDate("enrollment_date"));
            }
        }
        return rows;
    }

    public List<String> studentCourseReport() throws SQLException {
        String sql = """
                SELECT s.student_id, s.name AS student_name, c.course_id, c.course_name,
                       e.enrollment_date
                FROM enrollments e
                JOIN students s ON s.student_id = e.student_id
                JOIN courses c ON c.course_id = e.course_id
                ORDER BY s.name, c.course_name
                """;
        List<String> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) rows.add("Student: " + rs.getString("student_name") +
                    " (ID " + rs.getInt("student_id") + ") | Course: " +
                    rs.getString("course_name") + " (ID " + rs.getInt("course_id") +
                    ") | Date: " + rs.getDate("enrollment_date"));
        }
        return rows;
    }

    public List<String> callStudentCoursesProcedure(int studentId) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall("{call get_student_courses(?)}")) {
            cs.setInt(1, studentId);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) rows.add("Student: " + rs.getString("student_name") +
                        " | Course: " + rs.getString("course_name") +
                        " | Enrolled: " + rs.getDate("enrollment_date"));
            }
        }
        return rows;
    }

    public int batchEnroll(int studentId, List<Integer> courseIds) {
        String sql = "INSERT INTO enrollments(student_id, course_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (int courseId : courseIds) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, courseId);
                    ps.addBatch();
                }
                int[] counts = ps.executeBatch();
                con.commit();
                int total = 0;
                for (int count : counts) {
                    if (count == Statement.SUCCESS_NO_INFO) total++;
                    else if (count > 0) total += count;
                }
                return total;
            } catch (SQLException e) {
                try { con.rollback(); } catch (SQLException rollbackError) { e.addSuppressed(rollbackError); }
                throw new EnrollmentException("Batch enrollment failed; all changes were rolled back.", e);
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException ignored) { }
            }
        } catch (SQLException e) {
            throw new EnrollmentException("Could not get a database connection for batch enrollment.", e);
        }
    }
}
