package com.sisgroup6.demo.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public Student addStudent(Student student) {
        if (student.getId() != null) {
            Student existing = studentRepository.findById(student.getId()).orElse(null);
            if (existing != null) {
                existing.setFirstName(student.getFirstName());
                existing.setLastName(student.getLastName());
                existing.setEmail(student.getEmail());
                existing.setCourse(student.getCourse());
                if (student.getSection() != null) {
                    existing.setSection(student.getSection());
                }
                if (student.getStatus() != null) {
                    existing.setStatus(student.getStatus());
                }
                return studentRepository.save(existing);
            }
        }

        System.out.println("Processing new student: " + student.getFirstName());


        String generatedUsername = student.getFirstName().toLowerCase() + "." + student.getLastName().toLowerCase();
        student.setUsername(generatedUsername);

 
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); 
        student.setPassword("SIS-" + randomNumber);

        System.out.println("Generated Credentials -> Username: " + generatedUsername + " | Password: SIS-" + randomNumber);

        if (student.getStatus() == null) {
            student.setStatus("Pending");
        }

        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        System.out.println("Fetching all students from the database...");
        return studentRepository.findAll();
    }


    public String deleteStudent(Long id) {
        try {
            studentRepository.deleteById(id);
            System.out.println("Successfully deleted student with ID: " + id);
            return "Student deleted successfully!";
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return "Failed to delete student.";
        }
    }

    public Student updateProfile(Long id, String email, String newPassword) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));
        if (email != null && !email.trim().isEmpty()) {
            student.setEmail(email);
        }
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            student.setPassword(newPassword);
        }
        return studentRepository.save(student);
    }
}