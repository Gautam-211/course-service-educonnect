package com.example.courseservice.courseservice.controller;

import com.example.courseservice.courseservice.dto.CourseResponse;
import com.example.courseservice.courseservice.dto.CreateCourseRequest;
import com.example.courseservice.courseservice.dto.UpdateCourseRequest;
import com.example.courseservice.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ===========================
    // Public Endpoints
    // ===========================

    @GetMapping("/public")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/public/course-id/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/public/instructor-id/{instructorId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByInstructor(@PathVariable String instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    // ===========================
    // Instructor Endpoints
    // ===========================

    @PostMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PutMapping("/instructor/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/instructor/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/instructor/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> changeCourseStatus(
            @PathVariable Long id,
            @RequestParam boolean isPublished) {
        return ResponseEntity.ok(courseService.changeCourseStatus(id, isPublished));
    }
}
