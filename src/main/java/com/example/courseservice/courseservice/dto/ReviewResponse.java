package com.example.courseservice.courseservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private int rating;
    private String comment;
    private String userId;
    private LocalDateTime createdAt;
}
