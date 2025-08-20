package com.example.courseservice.courseservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String category;
    private double price;
    private boolean isPublished;
    private String thumbnailUrl;
    private String instructorId;

    private List<LectureResponse> lectures;
    private List<ReviewResponse> reviews;
}
