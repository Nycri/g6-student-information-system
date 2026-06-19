package com.sisgroup6.demo.Schedule; 

import com.sisgroup6.demo.Faculty.Faculty;
import com.sisgroup6.demo.Section.Section;
import com.sisgroup6.demo.Subject.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELATIONAL MAPPING (The Bridge) ---
    
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    // --- TIME AND SPACE VARIABLES ---
    
    private String room;
    
    private String dayOfWeek; // e.g., "Monday", "Tuesday"

    // No more @JsonFormat needed!
    private String startTime;
    private String endTime;

    // --- CONSTRUCTORS ---
    
    public Schedule() {
        // Default constructor required by Spring Boot / JPA
    }

    // --- GETTERS AND SETTERS ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}