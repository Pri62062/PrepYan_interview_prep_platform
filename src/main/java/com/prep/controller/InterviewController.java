package com.prep.controller;

import com.prep.entity.InterviewQuestion;
import com.prep.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")

public class InterviewController {

    @Autowired
    private InterviewService service;

    @PostMapping
    public InterviewQuestion add(@RequestBody InterviewQuestion q) {
        return service.save(q);
    }

    @GetMapping
    public List<InterviewQuestion> getAll() {
        return service.getAll();
    }

    @GetMapping("/subject/{subject}")
    public List<InterviewQuestion> getBySubject(@PathVariable String subject) {
        return service.getBySubject(subject);
    }
}
