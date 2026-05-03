package com.prep.service;

import com.prep.entity.Result;
import com.prep.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository repo;

    // 🔥 Save result
    public Result save(Result result) {
        return repo.save(result);
    }

    // 🔥 Get user results
    public List<Result> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }
}
