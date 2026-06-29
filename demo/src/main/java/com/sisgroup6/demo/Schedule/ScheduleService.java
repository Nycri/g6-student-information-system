package com.sisgroup6.demo.Schedule; // Change this to match your actual package name!


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import com.sisgroup6.demo.Student.Student;
import com.sisgroup6.demo.Student.StudentRepository;
import com.sisgroup6.demo.Enrollment.Enrollment;
import com.sisgroup6.demo.Enrollment.EnrollmentRepository;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // --- THE BOUNCER ALGORITHM ---
    
    public Schedule addSchedule(Schedule newSchedule) {
        
        // Step 1: Activate the Radar
        // We extract the variables from the incoming request and feed them into our custom SQL query.
        List<Schedule> conflicts = scheduleRepository.findConflicts(
                newSchedule.getDayOfWeek(),
                newSchedule.getFaculty().getId(),
                newSchedule.getSection().getId(),
                newSchedule.getRoom(),
                newSchedule.getStartTime(),
                newSchedule.getEndTime()
        );

        // Step 2: The Gatekeeper Decision
        if (newSchedule.getId() != null) {
            conflicts.removeIf(c -> c.getId().equals(newSchedule.getId()));
        }

        if (!conflicts.isEmpty()) {
            // A conflict was found! We throw an Exception to completely halt the execution.
            // This prevents the application from reaching the .save() method below.
            throw new RuntimeException("Scheduling Conflict Detected: The Room, Faculty, or Section is already booked during this time on " + newSchedule.getDayOfWeek() + ".");
        }

        // Step 3: The Safe Save
        // If the list is empty, it means the coast is clear. We officially save it to MySQL.
        return scheduleRepository.save(newSchedule);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    // --- STANDARD CRUD OPERATIONS ---

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public void deleteSchedule(Long id) {
        List<Enrollment> enrollments = enrollmentRepository.findByScheduleId(id);
        Set<Student> affectedStudents = new HashSet<>();
        for (Enrollment e : enrollments) {
            if (e.getStudent() != null) {
                affectedStudents.add(e.getStudent());
            }
        }
        
        enrollmentRepository.deleteAll(enrollments);
        scheduleRepository.deleteById(id);
        
        for (Student student : affectedStudents) {
            String sectionName = student.getSection();
            if (sectionName != null && !sectionName.trim().isEmpty()) {
                List<Schedule> sectionSchedules = scheduleRepository.findBySectionSectionName(sectionName);
                List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(student.getId());
                List<Long> enrolledIds = studentEnrollments.stream()
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
                } else {
                    student.setStatus("Pending");
                }
                studentRepository.save(student);
            }
        }
    }
}