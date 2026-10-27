package org.anjan;

import org.anjan.config.DBConnection;
import org.anjan.exception.*;
import org.anjan.model.Course;
import org.anjan.model.Student;
import org.anjan.service.CourseService;
import org.anjan.service.EnrollmentService;
import org.anjan.service.StudentService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final StudentService STUDENTS = new StudentService();
    private static final CourseService COURSES = new CourseService();
    private static final EnrollmentService ENROLLMENTS = new EnrollmentService();

    public static void main(String[] args) {
        System.out.println("Student Course Registration System");
        boolean running = true;
        try {
            while (running) {
                printMenu();
                int choice = readInt("Enter choice: ");
                try {
                    switch (choice) {
                        case 1 -> registerStudent();
                        case 2 -> printItems(STUDENTS.findAll());
                        case 3 -> System.out.println(STUDENTS.findById(readInt("Student ID: ")));
                        case 4 -> updateStudent();
                        case 5 -> STUDENTS.delete(readInt("Student ID to delete: "));
                        case 6 -> addCourse();
                        case 7 -> printItems(COURSES.findAll());
                        case 8 -> updateCourse();
                        case 9 -> COURSES.delete(readInt("Course ID to delete: "));
                        case 10 -> ENROLLMENTS.enroll(readInt("Student ID: "), readInt("Course ID: "));
                        case 11 -> printItems(ENROLLMENTS.studentsInCourse(readInt("Course ID: ")));
                        case 12 -> printItems(ENROLLMENTS.coursesForStudent(readInt("Student ID: ")));
                        case 13 -> printItems(ENROLLMENTS.report());
                        case 14 -> printItems(ENROLLMENTS.storedProcedure(readInt("Student ID: ")));
                        case 15 -> batchEnroll();
                        case 0 -> running = false;
                        default -> System.out.println("Invalid menu choice.");
                    }
                    if (choice >= 1 && choice <= 15) System.out.println("Operation completed.");
                } catch (StudentNotFoundException | CourseNotFoundException |
                         DuplicateStudentException | EnrollmentException |
                         IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        } finally {
            SCANNER.close();
            DBConnection.closePool();
            System.out.println("Application closed.");
        }
    }

    private static void printMenu() {
        System.out.println("""

                ===== STUDENT COURSE REGISTRATION SYSTEM =====
                1. Register Student
                2. View All Students
                3. Search Student by ID
                4. Update Student
                5. Delete Student
                6. Add Course
                7. View All Courses
                8. Update Course
                9. Delete Course
                10. Enroll Student in Course
                11. View Students Enrolled in a Course
                12. View Courses Taken by a Student
                13. Student-Course JOIN Report
                14. Stored Procedure: Courses for a Student
                15. Batch Enroll One Student in Multiple Courses
                0. Exit
                """);
    }

    private static void registerStudent() throws SQLException {
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String phone = readLine("Phone (optional): ");
        int id = STUDENTS.register(name, email, phone);
        System.out.println("Student registered. Generated ID: " + id);
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Student ID: ");
        String name = readLine("New name: ");
        String email = readLine("New email: ");
        String phone = readLine("New phone (optional): ");
        STUDENTS.update(id, name, email, phone);
    }

    private static void addCourse() throws SQLException {
        String name = readLine("Course name: ");
        int duration = readInt("Duration in months: ");
        BigDecimal fee = readDecimal("Fee: ");
        int id = COURSES.add(name, duration, fee);
        System.out.println("Course added. Generated ID: " + id);
    }

    private static void updateCourse() throws SQLException {
        int id = readInt("Course ID: ");
        String name = readLine("New course name: ");
        int duration = readInt("Duration in months: ");
        BigDecimal fee = readDecimal("Fee: ");
        COURSES.update(id, name, duration, fee);
    }

    private static void batchEnroll() {
        int studentId = readInt("Student ID: ");
        String raw = readLine("Course IDs separated by commas (e.g. 1,2,3): ");
        List<Integer> ids = Arrays.stream(raw.split(","))
                .map(String::trim).map(Integer::parseInt).toList();
        int count = ENROLLMENTS.batchEnroll(studentId, ids);
        System.out.println("Batch completed. Rows inserted: " + count);
    }

    private static int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readLine(prompt).trim()); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static BigDecimal readDecimal(String prompt) {
        while (true) {
            try { return new BigDecimal(readLine(prompt).trim()); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid decimal number."); }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    private static void printItems(List<?> items) {
        if (items.isEmpty()) System.out.println("No records found.");
        else items.forEach(System.out::println);
    }
}
