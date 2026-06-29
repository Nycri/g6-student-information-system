package com.sisgroup6.demo.Admin;

import com.sisgroup6.demo.Student.Student;
import com.sisgroup6.demo.Student.StudentRepository;
import com.sisgroup6.demo.Faculty.Faculty;
import com.sisgroup6.demo.Faculty.FacultyRepository;
import com.sisgroup6.demo.Section.SectionRepository;
import com.sisgroup6.demo.Subject.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        long totalStudents = studentRepository.count();
        long activeFaculty = facultyRepository.count();
        long totalSections = sectionRepository.count();
        long activeSubjects = subjectRepository.count();

        List<Student> recentStudents = studentRepository.findTop5ByOrderByIdDesc();
        List<Faculty> recentFaculty = facultyRepository.findTop5ByOrderByIdDesc();

        Map<String, Object> data = new HashMap<>();
        data.put("totalStudents", totalStudents);
        data.put("activeFaculty", activeFaculty);
        data.put("totalSections", totalSections);
        data.put("activeSubjects", activeSubjects);
        data.put("recentStudents", recentStudents);
        data.put("recentFaculty", recentFaculty);

        return ResponseEntity.ok(data);
    }
}
