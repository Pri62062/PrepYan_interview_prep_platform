package com.prep.repository;

import com.prep.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);

    void deleteByUserIdAndQuestionId(Long userId, Long questionId);
}
