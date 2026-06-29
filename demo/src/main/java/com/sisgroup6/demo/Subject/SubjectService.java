package com.sisgroup6.demo.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject addSubject(Subject subject) {
        System.out.println("Adding new subject: " + subject.getSubjectCode());
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public String deleteSubject(Long id) {
        try {
            subjectRepository.deleteById(id);
            return "Subject deleted successfully!";
        } catch (Exception e) {
            return "Failed to delete subject.";
        }
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }
}