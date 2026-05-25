package ch.fhnw.qtd.service;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.repository.CategoryRepository;
import ch.fhnw.qtd.repository.QuestionRepository;
import ch.fhnw.qtd.repository.SessionAnswerRepository;
import ch.fhnw.qtd.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardService {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionAnswerRepository sessionAnswerRepository;

    /** Returns the four top-level counts shown in the stat cards. */
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("categoryCount", categoryRepository.count());
        stats.put("questionCount", questionRepository.count());
        stats.put("sessionCount", sessionRepository.count());
        stats.put("answerCount", sessionAnswerRepository.count());
        return stats;
    }

    /** Returns one row per category: category, activeQuestions count, sessions count. */
    public List<Map<String, Object>> getCategoryBreakdown() {
        List<Category> categories = categoryRepository.findAll();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Category c : categories) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("category", c);
            row.put("activeQuestions", questionRepository.countByCategoryIdAndActive(c.getId(), true));
            row.put("sessions", sessionRepository.countByCategoryId(c.getId()));
            rows.add(row);
        }
        return rows;
    }
}