package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.*;
import com.example.courseservice.courseservice.entity.Course;
import com.example.courseservice.courseservice.exception.ResourceNotFoundException;
import com.example.courseservice.courseservice.kafka.KafkaProducerService;
import com.example.courseservice.courseservice.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public CourseResponse createCourse(CreateCourseRequest request) {
        String instructorId = getCurrentInstructorId();

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .thumbnailUrl(request.getThumbnailUrl())
                .instructorId(instructorId)
                .isPublished(false)
                .build();

        Course saved = courseRepository.save(course);
        CourseResponse response = mapToCourseResponse(saved);

        // Build the notification request
        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .userId(instructorId)
                .recipientEmail("gaminglytical2003@gmail.com")
                .subject("Course Created Successfully")
                .message("Your course '" + saved.getTitle() + "' has been created successfully.")
                .type("EMAIL")
                .build();

        // Wrap inside event
        CourseEvent event = new CourseEvent("COURSE_CREATED", notificationRequest);

        // Send event
        kafkaProducerService.sendMessage("course-events", event);

        return response;
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesByInstructor(String instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse changeCourseStatus(Long courseId, boolean isPublished) {
        Course course = getCourseOrThrow(courseId);
        course.setPublished(isPublished);
        Course saved = courseRepository.save(course);
        CourseResponse response = mapToCourseResponse(saved);

        // Build the notification request
        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .userId(course.getInstructorId())
                .recipientEmail("gaminglytical2003@gmail.com")
                .subject("Course Created Successfully")
                .message("Your course status has been changed to " + saved.isPublished())
                .type("EMAIL")
                .build();

        // Wrap inside event
        CourseEvent event = new CourseEvent("COURSE_STATUS_CHANGED", notificationRequest);

        // Send event
        kafkaProducerService.sendMessage("course-events", event);

        return response;
    }

    @Override
    @Cacheable(value = "courses", key = "#courseId")
    public CourseResponse getCourseById(Long courseId) {
        System.out.println("**********************************************");
        System.out.println("Fetching course data from DB with id : " + courseId);
        System.out.println("**********************************************");
        return mapToCourseResponse(getCourseOrThrow(courseId));
    }

    @Override
    @CacheEvict(value = "courses", key = "#courseId")
    public CourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = getCourseOrThrow(courseId);
        validateOwnership(course.getInstructorId());

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setPrice(request.getPrice());
        course.setThumbnailUrl(request.getThumbnailUrl());

        Course saved = courseRepository.save(course);
        CourseResponse response = mapToCourseResponse(saved);

        // Build the notification request
        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .userId(saved.getInstructorId())
                .recipientEmail("gaminglytical2003@gmail.com")
                .subject("Course Created Successfully")
                .message("Your course has been updated : " + saved)
                .type("EMAIL")
                .build();

        // Wrap inside event
        CourseEvent event = new CourseEvent("COURSE_UPDATED", notificationRequest);

        // Send event
        kafkaProducerService.sendMessage("course-events", event);

        return response;
    }

    @Override
    @CacheEvict(value = "courses", key = "#courseId")
    public void deleteCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);
        validateOwnership(course.getInstructorId());

        courseRepository.delete(course);
        CreateNotificationRequest notificationRequest = CreateNotificationRequest.builder()
                .userId(course.getInstructorId())
                .recipientEmail("gaminglytical2003@gmail.com")
                .subject("Course deleted Successfully")
                .message("Your course has been deleted : " + course)
                .type("EMAIL")
                .build();

        // Wrap inside event
        CourseEvent event = new CourseEvent("COURSE_DELETED", notificationRequest);

        // Send event
        kafkaProducerService.sendMessage("course-events", event);
    }

    // --------------------------
    // Helper Methods
    // --------------------------

    private CourseResponse mapToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .thumbnailUrl(course.getThumbnailUrl())
                .isPublished(course.isPublished())
                .instructorId(course.getInstructorId())
                .build();
    }

    //check
    private Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    private String getCurrentInstructorId() {
        return SecurityContextHolder.getContext().getAuthentication().getName(); // customize if token parsing is different
    }

    private void validateOwnership(String ownerId) {
        String requesterId = getCurrentInstructorId();
        if (!requesterId.equals(ownerId)) {
            throw new SecurityException("Unauthorized access. You do not own this course.");
        }
    }
}
