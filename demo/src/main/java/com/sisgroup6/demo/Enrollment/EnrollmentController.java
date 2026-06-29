package com.sisgroup6.demo.Enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.sisgroup6.demo.Student.Student;
import com.sisgroup6.demo.Student.StudentRepository;
import com.sisgroup6.demo.Schedule.Schedule;
import com.sisgroup6.demo.Schedule.ScheduleRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollment")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // 1. CREATE: Official Enrollment Trigger
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudent(@RequestBody Enrollment enrollment) {
        try {
            Enrollment savedEnrollment = enrollmentService.enrollStudent(enrollment);
            return new ResponseEntity<>(savedEnrollment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 2. UPDATE: Faculty Grade Encoding
    @PutMapping("/{id}/encode")
    public ResponseEntity<?> encodeGrades(@PathVariable Long id, @RequestBody Map<String, Double> grades) {
        try {
            Double prelim = grades.get("prelim");
            Double midterm = grades.get("midterm");
            Double finals = grades.get("finals");

            Enrollment updatedEnrollment = enrollmentService.encodeGrades(id, prelim, midterm, finals);
            return new ResponseEntity<>(updatedEnrollment, HttpStatus.OK);
        } catch (IllegalStateException e) {
            // Catches the "isSubmitted" lock guard
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 3. SECURE: Faculty Grade Submission Lock
    @PutMapping("/{id}/submit")
    public ResponseEntity<?> submitGrades(@PathVariable Long id) {
        try {
            Enrollment submittedEnrollment = enrollmentService.submitGrades(id);
            return new ResponseEntity<>(submittedEnrollment, HttpStatus.OK);
        } catch (RuntimeException e) {
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 4. READ: Fetch Student GWA
    @GetMapping("/student/{studentId}/gwa")
    public ResponseEntity<?> getStudentGWA(@PathVariable Long studentId) {
        try {
            Double gwa = enrollmentService.calculateStudentGWA(studentId);
            
            // We return a mapped JSON object so the frontend can easily read it
            return new ResponseEntity<>(Map.of(
                    "studentId", studentId,
                    "gwa", gwa
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/available-subjects/{studentId}")
    public ResponseEntity<?> getAvailableSubjects(@PathVariable Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found."));
            
            String sectionName = student.getSection();
            if (sectionName == null || sectionName.trim().isEmpty()) {
                return ResponseEntity.ok(new ArrayList<Schedule>());
            }
            
            List<Schedule> schedules = scheduleRepository.findBySectionSectionName(sectionName);
            
            // Also filter out schedules the student is already enrolled in
            List<Enrollment> currentEnrollments = enrollmentRepository.findByStudentId(studentId);
            List<Long> enrolledScheduleIds = currentEnrollments.stream()
                    .map(e -> e.getSchedule().getId())
                    .collect(Collectors.toList());
                    
            List<Schedule> availableSchedules = schedules.stream()
                    .filter(s -> !enrolledScheduleIds.contains(s.getId()))
                    .collect(Collectors.toList());
                    
            return ResponseEntity.ok(availableSchedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enroll-batch")
    public ResponseEntity<?> enrollBatch(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found."));
                    
            List<?> scheduleIdsObj = (List<?>) payload.get("scheduleIds");
            List<Enrollment> enrolledList = new ArrayList<>();
            
            for (Object idObj : scheduleIdsObj) {
                Long scheduleId = Long.valueOf(idObj.toString());
                Schedule schedule = scheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new RuntimeException("Schedule block not found for ID: " + scheduleId));
                        
                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setSchedule(schedule);
                enrollment.setSubmitted(false); // starts as pending / unsubmitted grades
                
                enrolledList.add(enrollmentService.enrollStudent(enrollment));
            }
            
            // Check if student has now enrolled in all available schedules for their section
            if (student.getSection() != null && !student.getSection().trim().isEmpty()) {
                List<Schedule> sectionSchedules = scheduleRepository.findBySectionSectionName(student.getSection());
                List<Enrollment> enrollmentsAfter = enrollmentRepository.findByStudentId(studentId);
                List<Long> enrolledIds = enrollmentsAfter.stream()
                        .map(e -> e.getSchedule().getId())
                        .collect(Collectors.toList());
                        
                boolean enrolledAll = true;
                for (Schedule s : sectionSchedules) {
                    if (!enrolledIds.contains(s.getId())) {
                        enrolledAll = false;
                        break;
                    }
                }
                
                if (enrolledAll && !sectionSchedules.isEmpty()) {
                    student.setStatus("Enrolled");
                    studentRepository.save(student);
                }
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(enrolledList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static boolean enrollmentOpen = true;

    @GetMapping("/is-open")
    public ResponseEntity<?> isEnrollmentOpen() {
        Map<String, Object> response = new HashMap<>();
        response.put("open", enrollmentOpen);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle-status")
    public ResponseEntity<?> toggleEnrollmentStatus() {
        enrollmentOpen = !enrollmentOpen;
        Map<String, Object> response = new HashMap<>();
        response.put("open", enrollmentOpen);
        return ResponseEntity.ok(response);
    }
}