package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CreateReviewRequest;
import com.example.courseservice.courseservice.dto.ReviewResponse;
import com.example.courseservice.courseservice.entity.Course;
import com.example.courseservice.courseservice.entity.Review;
import com.example.courseservice.courseservice.exception.ResourceNotFoundException;
import com.example.courseservice.courseservice.repository.CourseRepository;
import com.example.courseservice.courseservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

    @Override
    public ReviewResponse createReview(CreateReviewRequest request) {
        log.info("Creating review for course ID: {}", request.getCourseId());
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .userId(request.getUserId())
                .course(course)
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        log.info("Review created with ID: {}", savedReview.getId());
        return mapToResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getAllReviewsForCourse(Long courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
        return mapToResponse(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        log.info("Deleting review with ID: {}", reviewId);
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);

        log.info("Review with ID: {} deleted successfully", reviewId);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUserId())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
