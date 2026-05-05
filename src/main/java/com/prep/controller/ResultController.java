package com.prep.controller;

import com.prep.entity.Result;
import com.prep.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")

public class ResultController {

    @Autowired
    private ResultService service;

    // 🔥 Save result
    @PostMapping
    public Result save(@RequestBody Result result) {
        return service.save(result);
    }

    // 🔥 Get results by user
    @GetMapping("/user/{userId}")
    public List<Result> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }
}
