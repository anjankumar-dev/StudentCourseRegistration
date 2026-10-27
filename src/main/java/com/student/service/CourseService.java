package com.student.service;

import com.student.dao.CourseDAO;
import com.student.model.Course;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO dao = new CourseDAO();

    public int add(String name, int duration, BigDecimal fee) throws SQLException {
        validate(name, duration, fee);
        return dao.insert(new Course(name.trim(), duration, fee));
    }
    public List<Course> findAll() throws SQLException { return dao.findAll(); }
    public Course findById(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Course ID must be greater than zero.");
        return dao.findById(id);
    }
    public void update(int id, String name, int duration, BigDecimal fee) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Course ID must be greater than zero.");
        validate(name, duration, fee);
        dao.update(new Course(id, name.trim(), duration, fee));
    }
    public void delete(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Course ID must be greater than zero.");
        dao.delete(id);
    }
    private void validate(String name, int duration, BigDecimal fee) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Course name cannot be empty.");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be greater than zero.");
        if (fee == null || fee.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Fee cannot be negative.");
    }
}
