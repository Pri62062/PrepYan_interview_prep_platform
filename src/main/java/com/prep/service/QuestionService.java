package com.prep.service;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    // 🔥 LIGHTWEIGHT LIST (SAFE FOR RENDER)
    public List<QuestionDTO> getAllDTO() {

        System.out.println("📦 Fetching questions...");

        try {
            return repo.findAll(PageRequest.of(0, 5, Sort.by("id").descending())) // 🔥 reduced to 5
                    .getContent()
                    .stream()
                    .map(q -> new QuestionDTO(
                            q.getId(),
                            q.getTitle(),
                            q.getTopic(),
                            q.getDifficulty(),
                            null,   // keep light
                            null
                    ))
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching questions");
        }
    }

    // ✅ GET BY ID (SAFE)
    public Question getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // ✅ SAVE
    public Question save(Question q) {
        return repo.save(q);
    }

    // ✅ UPDATE
    public Question update(Long id, Question q) {
        Question existing = getById(id);

        existing.setTitle(q.getTitle());
        existing.setDescription(q.getDescription());
        existing.setTopic(q.getTopic());
        existing.setDifficulty(q.getDifficulty());
        existing.setAnswer(q.getAnswer());

        return repo.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // 🔥 SAFE LIMITED FETCH
    public List<Question> getLimited(int size) {
        return repo.findAll(PageRequest.of(0, Math.min(size, 5), Sort.by("id").descending()))
                .getContent();
    }

    // ✅ FILTERS (DB LEVEL - GOOD)
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