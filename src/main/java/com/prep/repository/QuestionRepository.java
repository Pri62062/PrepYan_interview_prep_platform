package com.prep.repository;

import com.prep.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // ✅ DB level filtering (NO memory issue)
    List<Question> findByTopicIgnoreCase(String topic);

    List<Question> findByDifficultyIgnoreCase(String difficulty);

    List<Question> findByTopicIgnoreCaseAndDifficultyIgnoreCase(String topic, String difficulty);
}