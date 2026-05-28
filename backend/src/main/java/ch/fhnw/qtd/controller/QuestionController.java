package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
@Tag(name = "Questions", description = "Manage discussion questions")
@SecurityRequirement(name = "basicAuth")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Operation(summary = "List questions",
               description = "Returns all questions, or filters by categoryId if provided.")
    @GetMapping
    public List<Question> getAllQuestions(@RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        if (categoryId != null) {
            return questionService.getQuestionsByCategory(categoryId, activeOnly);
        }
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Get a question by id")
    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @Operation(summary = "Create a new question")
    @PostMapping
    public Question createQuestion(@RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Long categoryId = ((Number) payload.get("categoryId")).longValue();
        return questionService.createQuestion(text, categoryId);
    }

    @Operation(summary = "Update an existing question")
    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        String text = (String) payload.get("text");
        Long categoryId = payload.get("categoryId") != null
                ? ((Number) payload.get("categoryId")).longValue() : null;
        Boolean active = (Boolean) payload.get("active");
        return questionService.updateQuestion(id, text, categoryId, active);
    }

    @Operation(summary = "Delete a question")
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}