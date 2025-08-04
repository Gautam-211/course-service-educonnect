package com.example.courseservice.courseservice.repository;

import com.example.courseservice.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<com.example.courseservice.courseservice.entity.Course> findByInstructorId(String instructorId);

    List<Course> findByIsPublishedTrue();

    List<Course> findByCategoryIgnoreCase(String category);
}
