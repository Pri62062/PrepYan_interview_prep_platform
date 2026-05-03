package com.prep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prep.entity.InterviewQuestion;
import com.prep.entity.Question;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findBySubject(String subject);
    
}