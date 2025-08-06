package com.example.courseservice.courseservice.controller;

import com.example.courseservice.courseservice.dto.CreateLectureRequest;
import com.example.courseservice.courseservice.dto.LectureResponse;
import com.example.courseservice.courseservice.service.LectureService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    // Create a new lecture
    @PostMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LectureResponse> createLecture(@Valid @RequestBody CreateLectureRequest request) {
        return ResponseEntity.ok(lectureService.createLecture(request));
    }

    // Get all lectures for a specific course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LectureResponse>> getLecturesByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(lectureService.getLecturesByCourseId(courseId));
    }

    // Get lecture by ID
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureResponse> getLectureById(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getLectureById(lectureId));
    }

    // Delete lecture by ID
    @DeleteMapping("/instructor/{lectureId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.noContent().build();
    }
}
