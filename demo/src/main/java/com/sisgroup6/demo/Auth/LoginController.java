package com.sisgroup6.demo.Auth;

import com.sisgroup6.demo.Admin.Admin;
import com.sisgroup6.demo.Admin.AdminRepository;
import com.sisgroup6.demo.Faculty.Faculty;
import com.sisgroup6.demo.Faculty.FacultyRepository;
import com.sisgroup6.demo.Student.Student;
import com.sisgroup6.demo.Student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Username and password are required.");
            return ResponseEntity.badRequest().body(error);
        }

        // 1. Check Admin
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                return createSuccessResponse(admin.getId(), admin.getUsername(), admin.getFullName(), admin.getEmail(), "ADMIN");
            }
        }

        // 2. Check Faculty
        Optional<Faculty> facultyOpt = facultyRepository.findByUsername(username);
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            if (faculty.getPassword().equals(password)) {
                String fullName = faculty.getFirstName() + " " + faculty.getLastName();
                return createSuccessResponse(faculty.getId(), faculty.getUsername(), fullName, faculty.getEmail(), "FACULTY");
            }
        }

        // 3. Check Student
        Optional<Student> studentOpt = studentRepository.findByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPassword().equals(password)) {
                String fullName = student.getFirstName() + " " + student.getLastName();
                return createSuccessResponse(student.getId(), student.getUsername(), fullName, student.getEmail(), "STUDENT");
            }
        }

        // If not found or password incorrect
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Invalid username or password.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String newPassword = body.get("newPassword");
        String role = body.get("role");

        if (username == null || email == null || newPassword == null || role == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "All fields are required."));
        }

        if (role.equalsIgnoreCase("ADMIN")) {
            Optional<Admin> adminOpt = adminRepository.findByUsername(username);
            if (adminOpt.isPresent() && adminOpt.get().getEmail().equalsIgnoreCase(email)) {
                Admin admin = adminOpt.get();
                admin.setPassword(newPassword);
                adminRepository.save(admin);
                return ResponseEntity.ok(Map.of("status", "success", "message", "Password reset successfully."));
            }
        } else if (role.equalsIgnoreCase("FACULTY")) {
            Optional<Faculty> facultyOpt = facultyRepository.findByUsername(username);
            if (facultyOpt.isPresent() && facultyOpt.get().getEmail().equalsIgnoreCase(email)) {
                Faculty faculty = facultyOpt.get();
                faculty.setPassword(newPassword);
                facultyRepository.save(faculty);
                return ResponseEntity.ok(Map.of("status", "success", "message", "Password reset successfully."));
            }
        } else if (role.equalsIgnoreCase("STUDENT")) {
            Optional<Student> studentOpt = studentRepository.findByUsername(username);
            if (studentOpt.isPresent() && studentOpt.get().getEmail().equalsIgnoreCase(email)) {
                Student student = studentOpt.get();
                student.setPassword(newPassword);
                studentRepository.save(student);
                return ResponseEntity.ok(Map.of("status", "success", "message", "Password reset successfully."));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "Invalid username, email, or role combination."));
    }

    private ResponseEntity<?> createSuccessResponse(Long id, String username, String fullName, String email, String role) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", username);
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("role", role);

        response.put("user", user);
        return ResponseEntity.ok(response);
    }
}
