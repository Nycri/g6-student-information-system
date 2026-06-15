package com.sisgroup6.demo.Section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public Section addSection(Section section) {
        System.out.println("Creating new section block: " + section.getSectionName() + " for course: " + section.getCourse());
        return sectionRepository.save(section);
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public String deleteSection(Long id) {
        try {
            sectionRepository.deleteById(id);
            return "Section block deleted successfully!";
        } catch (Exception e) {
            return "Failed to delete section block.";
        }
    }
}