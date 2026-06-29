package com.sisgroup6.demo.Enrollment;

import com.sisgroup6.demo.Schedule.Schedule;
import com.sisgroup6.demo.Student.Student;
import jakarta.persistence.*;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The Bridge Relationships
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    // The Grade Fields
    @Column(name = "prelim_grade")
    private Double prelimGrade;

    @Column(name = "midterm_grade")
    private Double midtermGrade;

    @Column(name = "finals_grade")
    private Double finalsGrade;

    @Column(name = "final_computed_grade")
    private Double finalComputedGrade;

    // The Security Flag
    @Column(name = "is_submitted", nullable = false)
    private boolean isSubmitted = false;

    // Constructors
    public Enrollment() {}

    // Getters and Setters
    public Long getId() {
         return id;
         }
    public void setId(Long id) {
         this.id = id;
         }
    public Student getStudent() {
          return student;
         }
    public void setStudent(Student student) {
          this.student = student;
         }
    public Schedule getSchedule() {
          return schedule; 
         }
    public void setSchedule(Schedule schedule) {
          this.schedule = schedule; 
         }
    public Double getPrelimGrade() {
          return prelimGrade;
      }
    public void setPrelimGrade(Double prelimGrade) { this.prelimGrade = prelimGrade; }
    public Double getMidtermGrade() { return midtermGrade; }
    public void setMidtermGrade(Double midtermGrade) { this.midtermGrade = midtermGrade; }
    public Double getFinalsGrade() { return finalsGrade; }
    public void setFinalsGrade(Double finalsGrade) { this.finalsGrade = finalsGrade; }
    public Double getFinalComputedGrade() { return finalComputedGrade; }
    public void setFinalComputedGrade(Double finalComputedGrade) { this.finalComputedGrade = finalComputedGrade; }
    public boolean isSubmitted() { return isSubmitted; }
    public void setSubmitted(boolean submitted) { isSubmitted = submitted; }
}