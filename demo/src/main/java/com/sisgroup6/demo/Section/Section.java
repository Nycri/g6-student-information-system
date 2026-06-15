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
    private int yearLevel;      
    private int maxCapacity;    


    public Section() {
    }

    public Section(String sectionName, String course, int yearLevel, int maxCapacity) {
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

    public int getYearLevel() {
      return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
      this.yearLevel = yearLevel;
    }

    public int getMaxCapacity() {
      return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
      this.maxCapacity = maxCapacity;
    }
  
    
}