package org.anjan.dao;

import org.anjan.config.DBConnection;
import org.anjan.exception.DuplicateStudentException;
import org.anjan.exception.StudentNotFoundException;
import org.anjan.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public int insert(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, email, phone) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            throw new SQLException("Insert succeeded but no generated student ID was returned.");
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState()) && e.getMessage() != null &&
                    e.getMessage().toLowerCase().contains("email")) {
                throw new DuplicateStudentException(student.getEmail());
            }
            throw e;
        }
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT student_id, name, email, phone FROM students ORDER BY student_id";
        List<Student> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT student_id, name, email, phone FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        throw new StudentNotFoundException(id);
    }

    public void update(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, email = ?, phone = ? WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setInt(4, student.getStudentId());
            if (ps.executeUpdate() == 0) throw new StudentNotFoundException(student.getStudentId());
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState()) && e.getMessage() != null &&
                    e.getMessage().toLowerCase().contains("email")) {
                throw new DuplicateStudentException(student.getEmail());
            }
            throw e;
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new StudentNotFoundException(id);
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("name"),
                rs.getString("email"), rs.getString("phone"));
    }
}
