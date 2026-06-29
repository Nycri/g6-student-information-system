package com.sisgroup6.demo.Student; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    List<Student> findTop5ByOrderByIdDesc();
    List<Student> findBySection(String section);
    List<Student> findByCourse(String course);
    long countBySection(String section);
}