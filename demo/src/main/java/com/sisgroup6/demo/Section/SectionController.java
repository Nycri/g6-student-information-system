package com.sisgroup6.demo.Section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/section")
@CrossOrigin 
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping("/add")
    public Section addSection(@RequestBody Section section) {
        return sectionService.addSection(section);
    }

    @GetMapping("/all")
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{id}")
    public Section getSectionById(@PathVariable Long id) {
        return sectionService.getSectionById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSection(@PathVariable Long id) {
        return sectionService.deleteSection(id);
    }
}