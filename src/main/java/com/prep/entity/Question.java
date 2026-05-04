package com.prep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "QUESTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String topic;
    private String difficulty;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String answer;
}