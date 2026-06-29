package com.sisgroup6.demo.Enrollment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // 1. CREATE: Officially links a student to a class schedule
    public Enrollment enrollStudent(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // 2. UPDATE: The "Calculator" and "Security Guard" for encoding grades
    public Enrollment encodeGrades(Long enrollmentId, Double prelim, Double midterm, Double finals) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment record not found."));

        // 🛑 The Guard: Check if grades are locked
        if (enrollment.isSubmitted()) {
            throw new IllegalStateException("Grades have already been submitted and cannot be changed.");
        }

        // Apply new grades
        enrollment.setPrelimGrade(prelim);
        enrollment.setMidtermGrade(midterm);
        enrollment.setFinalsGrade(finals);

        // 🧮 The Math: Execute calculation only if all three grades are present
        if (prelim != null && midterm != null && finals != null) {
            double finalGrade = (prelim * 0.3) + (midterm * 0.3) + (finals * 0.4);
            
            // Round to 2 decimal places for clean DB storage
            finalGrade = Math.round(finalGrade * 100.0) / 100.0;
            enrollment.setFinalComputedGrade(finalGrade);
        }

        return enrollmentRepository.save(enrollment);
    }

    // 3. SECURE: Submits and locks the grades permanently
    public Enrollment submitGrades(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment record not found."));

        enrollment.setSubmitted(true);
        return enrollmentRepository.save(enrollment);
    }
    // 4. READ: Calculate Student's General Weighted Average (GWA)
    public Double calculateStudentGWA(Long studentId) {
        // Step A: Get all classes the student is enrolled in
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        
        double totalGrades = 0.0;
        int completedSubjects = 0;

        // Step B: Loop through the enrollments
        for (Enrollment enrollment : enrollments) {
            // Only count the grade IF the professor has officially submitted it
            if (enrollment.isSubmitted() && enrollment.getFinalComputedGrade() != null) {
                totalGrades += enrollment.getFinalComputedGrade();
                completedSubjects++;
            }
        }

        // Step C: Prevent dividing by zero if the student has no completed classes yet
        if (completedSubjects == 0) {
            return 0.0; 
        }

        // Step D: Calculate and round to 2 decimal places
        double gwa = totalGrades / completedSubjects;
        return Math.round(gwa * 100.0) / 100.0;
    }
}