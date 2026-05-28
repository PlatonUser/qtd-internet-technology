package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.model.SessionAnswer;
import ch.fhnw.qtd.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
@Tag(name = "Sessions", description = "Manage game sessions and player answers")
@SecurityRequirement(name = "basicAuth")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Operation(summary = "List all sessions", description = "Returns all sessions ordered newest first.")
    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @Operation(summary = "Get a session by id")
    @GetMapping("/{id}")
    public Session getSession(@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    @Operation(summary = "Start a new session",
               description = "Creates a session for a category with the listed players and picks 5 random active questions.")
    @PostMapping
    @SuppressWarnings("unchecked")
    public Session createSession(@RequestBody Map<String, Object> payload) {
        Long categoryId = ((Number) payload.get("categoryId")).longValue();
        List<String> players = (List<String>) payload.getOrDefault("players", Collections.emptyList());
        return sessionService.createSession(categoryId, players);
    }

    @Operation(summary = "Mark a session as completed")
    @PutMapping("/{id}/complete")
    public Session completeSession(@PathVariable Long id) {
        return sessionService.completeSession(id);
    }

    @Operation(summary = "Delete a session")
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
    }

    @Operation(summary = "Get the questions in a session")
    @GetMapping("/{id}/questions")
    public List<Question> getQuestionsForSession(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "5") int limit) {
        return sessionService.getQuestionsForSession(id, limit);
    }

    @Operation(summary = "Add an answer to a session")
    @PostMapping("/{id}/answers")
    public SessionAnswer addAnswer(@PathVariable Long id,
                                   @RequestBody Map<String, Object> payload) {
        Long questionId = ((Number) payload.get("questionId")).longValue();
        String playerName = (String) payload.get("playerName");
        String answerText = (String) payload.get("answerText");
        return sessionService.addAnswer(id, questionId, playerName, answerText);
    }

    @Operation(summary = "Get all answers for a session")
    @GetMapping("/{id}/answers")
    public List<SessionAnswer> getAnswers(@PathVariable Long id) {
        return sessionService.getAnswersForSession(id);
    }

    @Operation(summary = "Count answers for a session")
    @GetMapping("/{id}/answer-count")
    public long getAnswerCount(@PathVariable Long id) {
        return sessionService.countAnswersForSession(id);
    }
}