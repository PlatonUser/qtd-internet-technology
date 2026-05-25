package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.model.SessionAnswer;
import ch.fhnw.qtd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public Session createSession(@RequestBody Map<String, Object> payload) {
        Long categoryId = ((Number) payload.get("categoryId")).longValue();
        List<String> players = (List<String>) payload.getOrDefault("players", Collections.emptyList());
        return sessionService.createSession(categoryId, players);
    }

    @PutMapping("/{id}/complete")
    public Session completeSession(@PathVariable Long id) {
        return sessionService.completeSession(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
    }

    @GetMapping("/{id}/questions")
    public List<Question> getQuestionsForSession(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "5") int limit) {
        return sessionService.getQuestionsForSession(id, limit);
    }

    @PostMapping("/{id}/answers")
    public SessionAnswer addAnswer(@PathVariable Long id,
                                   @RequestBody Map<String, Object> payload) {
        Long questionId = ((Number) payload.get("questionId")).longValue();
        String playerName = (String) payload.get("playerName");
        String answerText = (String) payload.get("answerText");
        return sessionService.addAnswer(id, questionId, playerName, answerText);
    }

    @GetMapping("/{id}/answers")
    public List<SessionAnswer> getAnswers(@PathVariable Long id) {
        return sessionService.getAnswersForSession(id);
    }

    @GetMapping("/{id}/answer-count")
    public long getAnswerCount(@PathVariable Long id) {
        return sessionService.countAnswersForSession(id);
    }
}