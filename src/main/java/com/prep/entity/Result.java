package com.prep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long questionId;

    private String userAnswer;

    private boolean correct;

    private int score;
}