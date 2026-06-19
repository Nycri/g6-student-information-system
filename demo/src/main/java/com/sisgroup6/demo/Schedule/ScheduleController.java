package com.sisgroup6.demo.Schedule; // Change this to match your actual package name!

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin // Crucial for allowing your Vanilla JS/jQuery frontend to talk to this API
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // --- THE MESSENGER (POST / ADD) ---
    
    @PostMapping("/add")
    public ResponseEntity<?> addSchedule(@RequestBody Schedule newSchedule) {
        try {
            // Step 1: Try to pass the incoming data to the Service (The Bouncer)
            Schedule savedSchedule = scheduleService.addSchedule(newSchedule);
            
            // Step 2: If the Bouncer allows it, send a 200 OK success status back to the frontend
            return new ResponseEntity<>(savedSchedule, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            // Step 3: If the Bouncer detects a conflict, catch the exception here!
            // We take the exact string message we wrote in the Service and package it as a 409 Conflict.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    // --- STANDARD CRUD ENDPOINTS ---

    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>("Schedule deleted successfully.", HttpStatus.OK);
    }
}