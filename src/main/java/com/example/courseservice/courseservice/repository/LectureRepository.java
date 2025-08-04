package com.example.courseservice.courseservice.repository;

import com.example.courseservice.courseservice.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByCourseId(Long courseId);
}
