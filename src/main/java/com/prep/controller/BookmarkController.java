package com.prep.controller;

import com.prep.entity.Bookmark;
import com.prep.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")

public class BookmarkController {

    @Autowired
    private BookmarkService service;

    // 🔥 Add bookmark
    @PostMapping
    public Bookmark save(@RequestBody Bookmark b) {
        return service.save(b);
    }

    // 🔥 Get bookmarks
    @GetMapping("/user/{userId}")
    public List<Bookmark> get(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    // 🔥 Delete bookmark
    @DeleteMapping
    public void delete(@RequestParam Long userId,
                       @RequestParam Long questionId) {
        service.delete(userId, questionId);
    }
}
