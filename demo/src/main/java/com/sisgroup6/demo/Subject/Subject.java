package com.sisgroup6.demo.Subject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subjectCode; 
    private String description; 
    private Integer units;         

    private String courseAssigned; 

    public Subject() {
    }

    public Subject(String subjectCode, String description, Integer units, String courseAssigned) {
        this.subjectCode = subjectCode;
        this.description = description;
        this.units = units;
        this.courseAssigned = courseAssigned;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSubjectCode() {
      return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
      this.subjectCode = subjectCode;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Integer getUnits() {
      return units;
    }

    public void setUnits(Integer units) {
      this.units = units;
    }

    public String getCourseAssigned() {
      return courseAssigned;
    }
    
}