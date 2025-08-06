package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CreateLectureRequest;
import com.example.courseservice.courseservice.dto.LectureResponse;

import java.util.List;

public interface LectureService {
    LectureResponse createLecture(CreateLectureRequest request);
    List<LectureResponse> getLecturesByCourseId(Long courseId);
    void deleteLecture(Long lectureId);
    LectureResponse getLectureById(Long lectureId);
}
