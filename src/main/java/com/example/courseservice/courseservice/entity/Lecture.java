package com.example.courseservice.courseservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Lecture title cannot be blank")
    @Size(min = 5, max = 100, message = "Lecture title must be between 5 to 100 characters")
    private String title;

    @NotBlank(message = "Video URL is required")
    private String videoUrl;

    @Positive(message = "Duration must be a positive number")
    private int durationInMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
