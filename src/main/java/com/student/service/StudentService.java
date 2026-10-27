package com.student.service;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public int register(String name, String email, String phone) throws SQLException {
        requireText(name, "Name");
        requireText(email, "Email");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        return dao.insert(new Student(name.trim(), email.trim(), blankToNull(phone)));
    }
    public List<Student> findAll() throws SQLException { return dao.findAll(); }
    public Student findById(int id) throws SQLException { requirePositive(id, "Student ID"); return dao.findById(id); }
    public void update(int id, String name, String email, String phone) throws SQLException {
        requirePositive(id, "Student ID");
        requireText(name, "Name");
        requireText(email, "Email");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Enter a valid email address.");
        dao.update(new Student(id, name.trim(), email.trim(), blankToNull(phone)));
    }
    public void delete(int id) throws SQLException { requirePositive(id, "Student ID"); dao.delete(id); }

    private static void requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(field + " cannot be empty.");
    }
    private static void requirePositive(int value, String field) {
        if (value <= 0) throw new IllegalArgumentException(field + " must be greater than zero.");
    }
    private static String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
