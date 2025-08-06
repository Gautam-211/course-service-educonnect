package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CreateReviewRequest;
import com.example.courseservice.courseservice.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest request);

    List<ReviewResponse> getAllReviewsForCourse(Long courseId);

    ReviewResponse getReviewById(Long reviewId);

    void deleteReview(Long reviewId);
}
