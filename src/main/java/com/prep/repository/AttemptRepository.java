package com.prep.repository;

import com.prep.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    // All attempts by a specific user
    List<Attempt> findByUserId(Long userId);

    // All attempts for a specific question
    List<Attempt> findByQuestionId(Long questionId);

    // All attempts by a user for a specific question
    List<Attempt> findByUserIdAndQuestionId(Long userId, Long questionId);

    // Count correct attempts
    long countByIsCorrect(Boolean isCorrect);

    // All correct attempts
    List<Attempt> findByIsCorrect(Boolean isCorrect);

    // Total attempts per user (for leaderboard / analytics)
    @Query("SELECT a.userId, COUNT(a) FROM Attempt a GROUP BY a.userId ORDER BY COUNT(a) DESC")
    List<Object[]> countByUserId();

    // Total attempts per question (for most-attempted chart)
    @Query("SELECT a.questionId, COUNT(a) FROM Attempt a GROUP BY a.questionId ORDER BY COUNT(a) DESC")
    List<Object[]> countByQuestionId();
}