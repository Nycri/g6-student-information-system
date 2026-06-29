package com.sisgroup6.demo.Section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.sisgroup6.demo.Student.Student;
import com.sisgroup6.demo.Student.StudentRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Section addSection(Section section) {
        System.out.println("Creating/updating section block: " + section.getSectionName() + " for course: " + section.getCourse());
        
        String oldSectionName = null;
        if (section.getId() != null) {
            Section oldSection = sectionRepository.findById(section.getId()).orElse(null);
            if (oldSection != null) {
                oldSectionName = oldSection.getSectionName();
            }
        }
        
        Section savedSection = sectionRepository.save(section);
        
        // Reset old section name from students who were in it
        if (oldSectionName != null) {
            List<Student> oldStudents = studentRepository.findBySection(oldSectionName);
            for (Student s : oldStudents) {
                s.setSection(null);
                studentRepository.save(s);
            }
        }
        
        // Assign new students to this section
        if (section.getStudentIds() != null) {
            for (Long studentId : section.getStudentIds()) {
                Student s = studentRepository.findById(studentId).orElse(null);
                if (s != null) {
                    s.setSection(savedSection.getSectionName());
                    studentRepository.save(s);
                }
            }
        }
        
        return savedSection;
    }

    public List<Section> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        for (Section s : sections) {
            long count = studentRepository.countBySection(s.getSectionName());
            s.setEnrolledCount(count);
        }
        return sections;
    }

    public String deleteSection(Long id) {
        try {
            Section section = sectionRepository.findById(id).orElse(null);
            if (section != null) {
                List<Student> sectionStudents = studentRepository.findBySection(section.getSectionName());
                for (Student s : sectionStudents) {
                    s.setSection(null);
                    studentRepository.save(s);
                }
            }
            sectionRepository.deleteById(id);
            return "Section block deleted successfully!";
        } catch (Exception e) {
            return "Failed to delete section block.";
        }
    }

    public Section getSectionById(Long id) {
        Section s = sectionRepository.findById(id).orElse(null);
        if (s != null) {
            s.setEnrolledCount(studentRepository.countBySection(s.getSectionName()));
        }
        return s;
    }
}