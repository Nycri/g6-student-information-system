package com.sisgroup6.demo.Schedule; // Change this to match your actual package name!


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

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
        if (!conflicts.isEmpty()) {
            // A conflict was found! We throw an Exception to completely halt the execution.
            // This prevents the application from reaching the .save() method below.
            throw new RuntimeException("Scheduling Conflict Detected: The Room, Faculty, or Section is already booked during this time on " + newSchedule.getDayOfWeek() + ".");
        }

        // Step 3: The Safe Save
        // If the list is empty, it means the coast is clear. We officially save it to MySQL.
        return scheduleRepository.save(newSchedule);
    }

    // --- STANDARD CRUD OPERATIONS ---

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}