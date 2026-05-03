package com.prep.controller;

import com.prep.entity.Question;
import com.prep.entity.User;
import com.prep.service.QuestionService;
import com.prep.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    // ✅ GET ALL USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ GET USER BY ID (🔥 THIS WAS MISSING)
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ UPDATE USER
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // ✅ DELETE USER (FIXED: return void)
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // ───────── QUESTIONS ─────────

    @PostMapping("/questions")
    public Question add(@RequestBody Question q) {
        return questionService.save(q);
    }

    @PutMapping("/questions/{id}")
    public Question update(@PathVariable Long id, @RequestBody Question q) {
        return questionService.update(id, q);
    }

    @DeleteMapping("/questions/{id}")
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}