package org.anjan.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(int id) { super("Course not found with ID: " + id); }
}
