package com.sisgroup6.demo.Faculty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @PostMapping("/add")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/all")
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }
}