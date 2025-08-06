package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CourseResponse;
import com.example.courseservice.courseservice.dto.CreateCourseRequest;
import com.example.courseservice.courseservice.dto.UpdateCourseRequest;
import com.example.courseservice.courseservice.entity.Course;
import com.example.courseservice.courseservice.exception.ResourceNotFoundException;
import com.example.courseservice.courseservice.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

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
                .isPublished(false) // default unpublished
                .build();

        Course saved = courseRepository.save(course);
        return mapToCourseResponse(saved);
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
        return mapToCourseResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse getCourseById(Long courseId) {
        return mapToCourseResponse(getCourseOrThrow(courseId));
    }

    @Override
    public CourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = getCourseOrThrow(courseId);
        validateOwnership(course.getInstructorId());

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setPrice(request.getPrice());
        course.setThumbnailUrl(request.getThumbnailUrl());

        return mapToCourseResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);
        validateOwnership(course.getInstructorId());

        courseRepository.delete(course);
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
