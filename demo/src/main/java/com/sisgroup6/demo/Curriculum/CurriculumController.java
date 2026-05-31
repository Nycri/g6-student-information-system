package com.sisgroup6.demo.Curriculum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
@CrossOrigin 
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;

    // API to get the list
    @GetMapping("/all")
    public List<Curriculum> viewAll() {
        System.out.println("Someone called the /all API!");
        return curriculumService.getAllCurriculum();
    }

    // API to add a new curriculum
    @PostMapping("/add")
    public String addCurriculum(@RequestBody Curriculum curriculum) {
        return curriculumService.saveCurriculum(curriculum);
    }

    // API to delete
    @DeleteMapping("/delete/{id}")
    public String deleteCurriculum(@PathVariable int id) {
        return curriculumService.deleteCurriculum(id);
    }
}