package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<Question> getAllQuestions(@RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        if (categoryId != null) {
            return questionService.getQuestionsByCategory(categoryId, activeOnly);
        }
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public Question createQuestion(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Long categoryId = ((Number) payload.get("categoryId")).longValue();
        return questionService.createQuestion(text, categoryId);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Long categoryId = payload.get("categoryId") != null
                ? ((Number) payload.get("categoryId")).longValue() : null;
        Boolean active = (Boolean) payload.get("active");
        return questionService.updateQuestion(id, text, categoryId, active);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}