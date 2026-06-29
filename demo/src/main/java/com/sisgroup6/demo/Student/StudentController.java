package com.sisgroup6.demo.Student;

import com.sisgroup6.demo.Enrollment.Enrollment;
import com.sisgroup6.demo.Enrollment.EnrollmentRepository;
import com.sisgroup6.demo.Enrollment.EnrollmentService;
import com.sisgroup6.demo.Schedule.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    } 

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/by-course/{course}")
    public List<Student> getStudentsByCourse(@PathVariable String course) {
        return studentRepository.findByCourse(course);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getStudentDashboard(@PathVariable Long id) {
        Double gwa = enrollmentService.calculateStudentGWA(id);
        
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);
        int totalUnits = enrollments.stream()
                .filter(e -> e.getSchedule() != null && e.getSchedule().getSubject() != null)
                .mapToInt(e -> e.getSchedule().getSubject().getUnits())
                .sum();

        Student student = studentService.getStudentById(id);
        Map<String, Object> stats = new HashMap<>();
        stats.put("gwa", gwa);
        stats.put("totalUnits", totalUnits);
        stats.put("enrollmentsCount", enrollments.size());
        stats.put("course", student != null ? student.getCourse() : "");
        stats.put("status", student != null ? student.getStatus() : "Pending");
        stats.put("section", student != null ? student.getSection() : "");
        
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateStudentProfile(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        Student student = studentService.updateProfile(id, email, newPassword);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}/grades")
    public List<Enrollment> getStudentGrades(@PathVariable Long id) {
        return enrollmentRepository.findByStudentId(id);
    }

    @GetMapping("/{id}/schedule")
    public List<Schedule> getStudentSchedule(@PathVariable Long id) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);
        return enrollments.stream()
                .map(Enrollment::getSchedule)
                .filter(sched -> sched != null)
                .collect(Collectors.toList());
    }
}