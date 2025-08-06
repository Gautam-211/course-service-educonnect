package com.example.courseservice.courseservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLectureRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String videoUrl;

    @Positive(message = "Duration must be a positive number")
    private int durationInMinutes;


    @NotNull
    private Long courseId;
}
