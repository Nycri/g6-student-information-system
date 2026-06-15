package com.sisgroup6.demo.Faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    public Faculty addFaculty(Faculty faculty) {
        System.out.println("Processing new faculty member: " + faculty.getLastName());

        String generatedUsername = faculty.getFirstName().toLowerCase().charAt(0) + faculty.getLastName().toLowerCase();
        faculty.setUsername(generatedUsername);


        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        faculty.setPassword("F-" + randomNumber);

        System.out.println("Faculty Credentials Generated -> Username: " + generatedUsername);

        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public String deleteFaculty(Long id) {
        try {
            facultyRepository.deleteById(id);
            return "Faculty member deleted successfully!";
        } catch (Exception e) {
            return "Failed to delete faculty member.";
        }
    }
}