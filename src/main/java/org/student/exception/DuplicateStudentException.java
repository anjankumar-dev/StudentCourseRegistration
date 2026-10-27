package org.student.exception;

public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String email) {
        super("A student with email '" + email + "' already exists.");
    }
}
