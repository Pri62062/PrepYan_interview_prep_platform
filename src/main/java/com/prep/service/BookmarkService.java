package com.prep.service;

import com.prep.entity.Bookmark;
import com.prep.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository repo;

    // 🔥 Add bookmark
    public Bookmark save(Bookmark b) {
        return repo.save(b);
    }

    // 🔥 Get bookmarks by user
    public List<Bookmark> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    // 🔥 Remove bookmark
    public void delete(Long userId, Long questionId) {
        repo.deleteByUserIdAndQuestionId(userId, questionId);
    }
}
