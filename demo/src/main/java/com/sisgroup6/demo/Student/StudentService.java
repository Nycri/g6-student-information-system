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
        System.out.println("Processing new student: " + student.getFirstName());


        String generatedUsername = student.getFirstName().toLowerCase() + "." + student.getLastName().toLowerCase();
        student.setUsername(generatedUsername);

 
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); 
        student.setPassword("SIS-" + randomNumber);

        System.out.println("Generated Credentials -> Username: " + generatedUsername + " | Password: SIS-" + randomNumber);

        return studentRepository.save(student);
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
}