package com.example.courseservice.courseservice.service;

import com.example.courseservice.courseservice.dto.CreateLectureRequest;
import com.example.courseservice.courseservice.dto.LectureResponse;
import com.example.courseservice.courseservice.entity.Course;
import com.example.courseservice.courseservice.entity.Lecture;
import com.example.courseservice.courseservice.exception.ResourceNotFoundException;
import com.example.courseservice.courseservice.repository.CourseRepository;
import com.example.courseservice.courseservice.repository.LectureRepository;
import com.example.courseservice.courseservice.service.LectureService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;

    @Override
    public LectureResponse createLecture(CreateLectureRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
                .videoUrl(request.getVideoUrl())
                .durationInMinutes(request.getDurationInMinutes())
                .course(course)
                .build();

        Lecture savedLecture = lectureRepository.save(lecture);

        return mapToLectureResponse(savedLecture);
    }

    @Override
    public List<LectureResponse> getLecturesByCourseId(Long courseId) {
        List<Lecture> lectures = lectureRepository.findByCourseId(courseId);
        return lectures.stream()
                .map(this::mapToLectureResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLecture(Long lectureId) {
        if (!lectureRepository.existsById(lectureId)) {
            throw new ResourceNotFoundException("Lecture not found with ID: " + lectureId);
        }
        lectureRepository.deleteById(lectureId);
    }

    @Override
    public LectureResponse getLectureById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found with id: " + lectureId));

        return mapToLectureResponse(lecture);
    }


    private LectureResponse mapToLectureResponse(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .videoUrl(lecture.getVideoUrl())
                .durationInMinutes(lecture.getDurationInMinutes())
                .build();
    }
}
