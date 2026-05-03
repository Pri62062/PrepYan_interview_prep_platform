package com.prep.repository;

import com.prep.entity.AiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiLogRepository extends JpaRepository<AiLog, Long> {

    // All logs for a specific user
    List<AiLog> findByUserId(Long userId);

    // Logs by status (completed / error)
    List<AiLog> findByStatus(String status);

    // Logs after a certain date (for daily trend)
    List<AiLog> findByTimestampAfter(LocalDateTime since);

    // Logs between two dates
    List<AiLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    // Count sessions per user (for most-active-users chart)
    @Query("SELECT a.userId, COUNT(a) FROM AiLog a GROUP BY a.userId ORDER BY COUNT(a) DESC")
    List<Object[]> countSessionsByUser();

    // Count distinct users who have used AI
    @Query("SELECT COUNT(DISTINCT a.userId) FROM AiLog a")
    long countDistinctUsers();
}