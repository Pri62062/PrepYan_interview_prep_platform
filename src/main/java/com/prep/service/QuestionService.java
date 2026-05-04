package com.prep.service;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ✅ Memory-safe QuestionService
 *
 * Problem: repo.findAll() loads ALL rows into heap at once.
 * With 500+ questions, this causes OutOfMemoryError on Render (256MB).
 *
 * Fix: Use pagination — load max 200 at a time.
 * 200 questions × ~2KB each = ~400KB → safe.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    // ✅ Safe max page size — adjust if needed
    private static final int MAX_QUESTIONS = 200;

    // ── Get all (paginated — memory safe) ─────
    public List<QuestionDTO> getAllDTO() {
        // PageRequest limits DB fetch — won't OOM
        return repo.findAll(PageRequest.of(0, MAX_QUESTIONS, Sort.by("id")))
                .getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Limited fetch for dashboard preview ───
    public List<QuestionDTO> getLimited(int size) {
        return repo.findAll(PageRequest.of(0, Math.min(size, 50), Sort.by("id")))
                .getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Get by ID ──────────────────────────────
    public Question getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found: " + id));
    }

    // ── Save ───────────────────────────────────
    public Question save(Question q) {
        return repo.save(q);
    }

    // ── Update ─────────────────────────────────
    public Question update(Long id, Question q) {
        Question existing = getById(id);
        existing.setTitle(q.getTitle());
        existing.setDescription(q.getDescription());
        existing.setTopic(q.getTopic());
        existing.setDifficulty(q.getDifficulty());
        if (q.getAnswer() != null) existing.setAnswer(q.getAnswer());
        return repo.save(existing);
    }

    // ── Delete ─────────────────────────────────
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Question not found: " + id);
        }
        repo.deleteById(id);
    }

    // ── Filter by topic ────────────────────────
    public List<QuestionDTO> getByTopic(String topic) {
        return repo.findByTopicIgnoreCase(topic)
                .stream().limit(MAX_QUESTIONS)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Filter by difficulty ───────────────────
    public List<QuestionDTO> getByDifficulty(String difficulty) {
        return repo.findByDifficultyIgnoreCase(difficulty)
                .stream().limit(MAX_QUESTIONS)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Filter by topic + difficulty ──────────
    public List<QuestionDTO> getByTopicAndDifficulty(String topic, String difficulty) {
        return repo.findByTopicIgnoreCaseAndDifficultyIgnoreCase(topic, difficulty)
                .stream().limit(MAX_QUESTIONS)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Entity → DTO (keeps heap small) ───────
    // Only expose fields frontend needs
    // Don't expose full entity — prevents accidental large object loading
    private QuestionDTO toDTO(Question q) {
        return new QuestionDTO(
                q.getId(),
                q.getTitle(),
                q.getTopic(),
                q.getDifficulty(),
                q.getDescription(),
                null   // ✅ Don't send answer in list — only send on single GET
        );
    }
}