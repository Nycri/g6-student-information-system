package com.sisgroup6.demo.Curriculum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    CurriculumRepository curriculumRepo;

    public List<Curriculum> getAllCurriculum() {
        System.out.println("Fetching all curriculum from database...");
        return curriculumRepo.findAll();
    }

    public String saveCurriculum(Curriculum newCurriculum) {
        try {
            curriculumRepo.save(newCurriculum);
            System.out.println("Saved successfully!");
            return "Curriculum saved to database!";
        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return "Failed to save curriculum.";
        }
    }

    public String deleteCurriculum(int id) {
        try {
            curriculumRepo.deleteById(id);
            return "Curriculum deleted!";
        } catch (Exception e) {
            return "Error: Could not find ID to delete.";
        }
    }

    public Curriculum getCurriculumById(int id) {
        return curriculumRepo.findById(id).orElse(null);
    }
}