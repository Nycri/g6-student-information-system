package com.sisgroup6.demo.Section;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sectionName; 
    private String course;     
    private Integer yearLevel;      
    private Integer maxCapacity;    


    public Section() {
    }

    public Section(String sectionName, String course, Integer yearLevel, Integer maxCapacity) {
        this.sectionName = sectionName;
        this.course = course;
        this.yearLevel = yearLevel;
        this.maxCapacity = maxCapacity;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSectionName() {
      return sectionName;
    }

    public void setSectionName(String sectionName) {
      this.sectionName = sectionName;
    }

    public String getCourse() {
      return course;
    }

    public void setCourse(String course) {
      this.course = course;
    }

    public Integer getYearLevel() {
      return yearLevel;
    }

    public void setYearLevel(Integer yearLevel) {
      this.yearLevel = yearLevel;
    }

    public Integer getMaxCapacity() {
      return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
      this.maxCapacity = maxCapacity;
    }
  
    @jakarta.persistence.Transient
    private java.util.List<Long> studentIds;

    public java.util.List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(java.util.List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    @jakarta.persistence.Transient
    private Long enrolledCount;

    public Long getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(Long enrolledCount) {
        this.enrolledCount = enrolledCount;
    }
}