package com.example.courseservice.courseservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReviewRequest {

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;

    @NotBlank
    private String userId;

    @NotNull
    private Long courseId;
}
