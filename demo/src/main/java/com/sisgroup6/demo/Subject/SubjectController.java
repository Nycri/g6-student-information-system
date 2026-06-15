package com.sisgroup6.demo.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subject")
@CrossOrigin 
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/add")
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectService.addSubject(subject);
    }

    @GetMapping("/all")
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }
}