package ch.fhnw.qtd.service;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.repository.CategoryRepository;
import ch.fhnw.qtd.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired private QuestionRepository questionRepository;
    @Autowired private CategoryRepository categoryRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByCategory(Long categoryId, boolean activeOnly) {
        return activeOnly
                ? questionRepository.findByCategoryIdAndActive(categoryId, true)
                : questionRepository.findByCategoryId(categoryId);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question createQuestion(String text, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return null;
        return questionRepository.save(
                Question.builder()
                        .text(text)
                        .category(category)
                        .active(true)
                        .build());
    }

    public Question updateQuestion(Long id, String text, Long categoryId, Boolean active) {
        Question q = questionRepository.findById(id).orElse(null);
        if (q == null) return null;
        if (text != null) q.setText(text);
        if (categoryId != null) {
            Category c = categoryRepository.findById(categoryId).orElse(null);
            if (c != null) q.setCategory(c);
        }
        if (active != null) q.setActive(active);
        return questionRepository.save(q);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}