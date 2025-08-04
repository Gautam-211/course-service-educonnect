package com.example.courseservice.courseservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCourseRequest {

    @NotBlank
    private String title;

    @NotBlank
    @Size(min = 20)
    private String description;

    @NotBlank
    private String category;

    @PositiveOrZero
    private double price;

    @NotBlank
    private String thumbnailUrl;

    private boolean isPublished;
}
