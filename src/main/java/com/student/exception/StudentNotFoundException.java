package com.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(int id) { super("Student not found with ID: " + id); }
}
