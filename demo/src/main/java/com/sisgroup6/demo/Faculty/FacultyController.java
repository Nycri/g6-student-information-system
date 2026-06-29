package com.sisgroup6.demo.Faculty;

import com.sisgroup6.demo.Schedule.Schedule;
import com.sisgroup6.demo.Schedule.ScheduleRepository;
import com.sisgroup6.demo.Enrollment.Enrollment;
import com.sisgroup6.demo.Enrollment.EnrollmentRepository;
import com.sisgroup6.demo.Enrollment.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/add")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/all")
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<?> getFacultyDashboard(@PathVariable Long id) {
        List<Schedule> schedules = scheduleRepository.findByFacultyId(id);
        List<Enrollment> enrollments = enrollmentRepository.findByScheduleFacultyId(id);

        long activeClassesCount = schedules.size();

        long totalStudentsCount = enrollments.stream()
                .map(e -> e.getStudent().getId())
                .distinct()
                .count();

        // Calculate pending grades count: schedules where at least one enrollment is not submitted
        // First group enrollments by schedule id
        Map<Long, List<Enrollment>> enrollmentsBySchedule = enrollments.stream()
                .collect(Collectors.groupingBy(e -> e.getSchedule().getId()));

        long pendingGradesCount = 0;
        for (Schedule schedule : schedules) {
            List<Enrollment> schedEnrollments = enrollmentsBySchedule.get(schedule.getId());
            if (schedEnrollments == null || schedEnrollments.isEmpty()) {
                pendingGradesCount++; // No students enrolled is technically pending or inactive
            } else {
                boolean hasUnsubmitted = schedEnrollments.stream().anyMatch(e -> !e.isSubmitted());
                if (hasUnsubmitted) {
                    pendingGradesCount++;
                }
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeClassesCount", activeClassesCount);
        stats.put("totalStudentsCount", totalStudentsCount);
        stats.put("pendingGradesCount", pendingGradesCount);

        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateFacultyProfile(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        Faculty faculty = facultyService.updateProfile(id, email, newPassword);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/{id}/schedule")
    public List<Schedule> getFacultySchedule(@PathVariable Long id) {
        return scheduleRepository.findByFacultyId(id);
    }

    @GetMapping("/{id}/classes/{scheduleId}/students")
    public List<Enrollment> getClassRoster(@PathVariable Long id, @PathVariable Long scheduleId) {
        return enrollmentRepository.findByScheduleId(scheduleId);
    }

    @PutMapping("/grades/update")
    public ResponseEntity<?> updateGrades(@RequestBody List<Map<String, Object>> gradeUpdates) {
        for (Map<String, Object> update : gradeUpdates) {
            Long enrollmentId = Long.valueOf(update.get("enrollmentId").toString());
            Double prelim = update.get("prelimGrade") != null ? Double.valueOf(update.get("prelimGrade").toString()) : null;
            Double midterm = update.get("midtermGrade") != null ? Double.valueOf(update.get("midtermGrade").toString()) : null;
            Double finals = update.get("finalsGrade") != null ? Double.valueOf(update.get("finalsGrade").toString()) : null;

            enrollmentService.encodeGrades(enrollmentId, prelim, midterm, finals);
            
            if (update.get("submit") != null && Boolean.parseBoolean(update.get("submit").toString())) {
                enrollmentService.submitGrades(enrollmentId);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Grades updated successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/grades/submit/{enrollmentId}")
    public ResponseEntity<?> submitGrade(@PathVariable Long enrollmentId) {
        Enrollment enrollment = enrollmentService.submitGrades(enrollmentId);
        return ResponseEntity.ok(enrollment);
    }
}