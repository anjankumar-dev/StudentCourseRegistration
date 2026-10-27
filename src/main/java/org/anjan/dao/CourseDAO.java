package org.anjan.dao;

import org.anjan.config.DBConnection;
import org.anjan.exception.CourseNotFoundException;
import org.anjan.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public int insert(Course course) throws SQLException {
        String sql = "INSERT INTO courses(course_name, duration_months, fee) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getDurationMonths());
            ps.setBigDecimal(3, course.getFee());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            throw new SQLException("Insert succeeded but no generated course ID was returned.");
        }
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT course_id, course_name, duration_months, fee FROM courses ORDER BY course_id";
        List<Course> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Course findById(int id) throws SQLException {
        String sql = "SELECT course_id, course_name, duration_months, fee FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        throw new CourseNotFoundException(id);
    }

    public void update(Course course) throws SQLException {
        String sql = "UPDATE courses SET course_name = ?, duration_months = ?, fee = ? WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getDurationMonths());
            ps.setBigDecimal(3, course.getFee());
            ps.setInt(4, course.getCourseId());
            if (ps.executeUpdate() == 0) throw new CourseNotFoundException(course.getCourseId());
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new CourseNotFoundException(id);
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        return new Course(rs.getInt("course_id"), rs.getString("course_name"),
                rs.getInt("duration_months"), rs.getBigDecimal("fee"));
    }
}
