package com.prep.service;

import com.prep.entity.InterviewQuestion;
import com.prep.repository.InterviewQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    @Autowired
    private InterviewQuestionRepository repo;

    public InterviewQuestion save(InterviewQuestion q) {
        return repo.save(q);
    }

    public List<InterviewQuestion> getAll() {
        return repo.findAll();
    }

    public List<InterviewQuestion> getBySubject(String subject) {
        return repo.findBySubject(subject);
    }
}
