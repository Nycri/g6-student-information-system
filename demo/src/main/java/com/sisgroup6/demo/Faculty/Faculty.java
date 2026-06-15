package com.sisgroup6.demo.Faculty; 

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String preferredSubjects;
    private String username;
    private String password;

    public Faculty() {
    }

    public Faculty(String firstName, String lastName, String email, String department, String preferredSubjects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.preferredSubjects = preferredSubjects;
    }

    public Long getId() {
       return id; 
      }
      
    public void setId(Long id) {
       this.id = id; 
      }
    public String getFirstName() {
       return firstName; 
      }
    public void setFirstName(String firstName) {
       this.firstName = firstName; 
      }
    public String getLastName() {
       return lastName; 
      }
    public void setLastName(String lastName) {
       this.lastName = lastName; 
      }
    public String getEmail() {
       return email; 
      }
    public void setEmail(String email) {
       this.email = email; 
      }
    public String getDepartment() {
       return department; 
      }
    public void setDepartment(String department) {
       this.department = department; 
      }
    public String getPreferredSubjects() {
       return preferredSubjects; 
      }
    public void setPreferredSubjects(String preferredSubjects) {
       this.preferredSubjects = preferredSubjects; 
      }
    public String getUsername() {
       return username; 
      }
    public void setUsername(String username) {
      this.username = username; 
      }
    public String getPassword() {
       return password; 
      }
    public void setPassword(String password) {
       this.password = password; 
      }
}