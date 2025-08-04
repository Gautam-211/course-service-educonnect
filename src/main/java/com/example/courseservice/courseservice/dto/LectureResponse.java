package com.example.courseservice.courseservice.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureResponse {

    private Long id;
    private String title;
    private String videoUrl;
    private String duration;
}
