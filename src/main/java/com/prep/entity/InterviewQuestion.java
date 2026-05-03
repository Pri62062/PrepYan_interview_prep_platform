package com.prep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "INTERVIEW_QUESTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;  

    @Lob
    private String question;

    @Lob
    private String answer;
}
