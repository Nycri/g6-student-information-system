package com.sisgroup6.demo.Enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // NEW: Fetches all enrollments belonging to a specific student
    List<Enrollment> findByStudentId(Long studentId);

    // NEW: Fetches all enrollments belonging to a specific faculty
    List<Enrollment> findByScheduleFacultyId(Long facultyId);

    // NEW: Fetches all enrollments for a specific schedule block
    List<Enrollment> findByScheduleId(Long scheduleId);
}