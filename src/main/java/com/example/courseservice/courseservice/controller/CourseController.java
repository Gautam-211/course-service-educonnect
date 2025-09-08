package com.example.courseservice.courseservice.controller;

import com.example.courseservice.courseservice.dto.CourseResponse;
import com.example.courseservice.courseservice.dto.CreateCourseRequest;
import com.example.courseservice.courseservice.dto.UpdateCourseRequest;
import com.example.courseservice.courseservice.service.CourseService;
import com.example.courseservice.courseservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final FileUploadService fileUploadService;

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

    @GetMapping("/public/{id}/exists")
    public ResponseEntity<Boolean> courseExists(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.courseExists(id));
    }

    // ===========================
    // Instructor Endpoints
    // ===========================

    @PostMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> createCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") double price,
            @RequestPart("thumbnail") MultipartFile thumbnail, // Use @RequestPart for the file
            @RequestHeader("Authorization") String authHeader
    ) {

        String thumbnailUrl = fileUploadService.uploadFile(thumbnail, "course-thumbnails");
        // Manually build the request object
        CreateCourseRequest request = CreateCourseRequest.builder()
                .title(title)
                .description(description)
                .category(category)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .build();


        String token = authHeader.substring(7);
        return ResponseEntity.ok(courseService.createCourse(request, token));
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
