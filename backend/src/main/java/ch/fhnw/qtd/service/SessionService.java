package ch.fhnw.qtd.service;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.model.SessionAnswer;
import ch.fhnw.qtd.repository.CategoryRepository;
import ch.fhnw.qtd.repository.QuestionRepository;
import ch.fhnw.qtd.repository.SessionAnswerRepository;
import ch.fhnw.qtd.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionAnswerRepository answerRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private QuestionRepository questionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAllByOrderByStartedAtDesc();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElse(null);
    }

    public Session createSession(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return null;
        return sessionRepository.save(
                Session.builder()
                        .category(category)
                        .startedAt(LocalDateTime.now())
                        .completed(false)
                        .build());
    }

    public Session completeSession(Long id) {
        Session session = sessionRepository.findById(id).orElse(null);
        if (session == null) return null;
        session.setCompleted(true);
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public List<Question> getQuestionsForSession(Long sessionId, int limit) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null || session.getCategory() == null) return Collections.emptyList();
        List<Question> all = questionRepository.findByCategoryIdAndActive(session.getCategory().getId(), true);
        Collections.shuffle(all);
        return all.stream().limit(limit).collect(Collectors.toList());
    }

    public SessionAnswer addAnswer(Long sessionId, Long questionId, String playerName, String answerText) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) return null;
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null) return null;
        return answerRepository.save(
                SessionAnswer.builder()
                        .session(session)
                        .question(question)
                        .playerName(playerName)
                        .answerText(answerText)
                        .build());
    }

    public List<SessionAnswer> getAnswersForSession(Long sessionId) {
        return answerRepository.findBySessionId(sessionId);
    }

    public long countAnswersForSession(Long sessionId) {
        return answerRepository.countBySessionId(sessionId);
    }
}