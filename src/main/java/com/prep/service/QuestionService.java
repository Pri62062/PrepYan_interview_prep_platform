package com.prep.service;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    // ✅ SAFE: limited + DTO
    public List<QuestionDTO> getAllDTO() {
        return repo.findAll(PageRequest.of(0, 10))
                .getContent()
                .stream()
                .map(q -> new QuestionDTO(
                	    q.getId(),
                	    q.getTitle(),
                	    q.getTopic(),
                	    q.getDifficulty(),
                	    q.getDescription(),   // ✅ ADD
                	    q.getAnswer()         // ✅ ADD
                	))
                .toList();
    }

    // ✅ LIMIT METHOD (extra safety)
    public List<Question> getLimited(int size) {
        return repo.findAll(PageRequest.of(0, size)).getContent();
    }

    public Question getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question save(Question q) {
        return repo.save(q);
    }

    public Question update(Long id, Question q) {
        Question existing = getById(id);
        existing.setTitle(q.getTitle());
        existing.setDescription(q.getDescription());
        existing.setTopic(q.getTopic());
        existing.setDifficulty(q.getDifficulty());
        existing.setAnswer(q.getAnswer());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ FIXED: DB filtering (NO findAll().stream())
    public List<Question> getByTopic(String topic) {
        return repo.findByTopicIgnoreCase(topic);
    }

    public List<Question> getByDifficulty(String difficulty) {
        return repo.findByDifficultyIgnoreCase(difficulty);
    }

    public List<Question> getByTopicAndDifficulty(String topic, String difficulty) {
        return repo.findByTopicIgnoreCaseAndDifficultyIgnoreCase(topic, difficulty);
    }
}