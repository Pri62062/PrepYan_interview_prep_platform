package com.prep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookmarks")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long questionId;
}