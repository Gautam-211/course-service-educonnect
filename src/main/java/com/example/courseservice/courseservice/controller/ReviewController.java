package com.example.courseservice.courseservice.controller;

import com.example.courseservice.courseservice.dto.CreateReviewRequest;
import com.example.courseservice.courseservice.dto.ReviewResponse;
import com.example.courseservice.courseservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Create a review
    @PostMapping("/student")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    // Get all reviews for a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getAllReviewsForCourse(courseId));
    }

    // Get review by ID
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    // Delete a review
    @DeleteMapping("/student/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
