package com.example.courseservice.courseservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseRequest {

    @NotBlank
    private String title;

    @NotBlank
    @Size(min = 20, max = 400)
    private String description;

    @NotBlank
    @Size(min = 2, max = 100)
    private String category;

    @PositiveOrZero
    private double price;

    @NotBlank
    private String thumbnailUrl;

}
