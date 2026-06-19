package com.sisgroup6.demo.Schedule; // Change this to match your actual package name!

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // --- THE CONFLICT RADAR ---
    
    @Query("SELECT s FROM Schedule s WHERE " +
           "s.dayOfWeek = :dayOfWeek AND " +
           "(s.faculty.id = :facultyId OR s.section.id = :sectionId OR s.room = :room) AND " +
           "(s.startTime < :endTime AND s.endTime > :startTime)")
List<Schedule> findConflicts(
            @Param("dayOfWeek") String dayOfWeek,
            @Param("facultyId") Long facultyId,
            @Param("sectionId") Long sectionId,
            @Param("room") String room,
            @Param("startTime") String startTime, // <-- Changed to String
            @Param("endTime") String endTime      // <-- Changed to String
    );
}