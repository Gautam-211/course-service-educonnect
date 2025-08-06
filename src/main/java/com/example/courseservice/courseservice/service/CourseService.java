package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CourseResponse;
import com.example.courseservice.courseservice.dto.CreateCourseRequest;
import com.example.courseservice.courseservice.dto.UpdateCourseRequest;


import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request);

    List<CourseResponse> getAllCourses();

    List<CourseResponse> getCoursesByInstructor(String instructorId);

    CourseResponse changeCourseStatus(Long courseId, boolean isPublished);

    CourseResponse getCourseById(Long courseId);

    CourseResponse updateCourse(Long courseId, UpdateCourseRequest request);

    void deleteCourse(Long courseId);
}

