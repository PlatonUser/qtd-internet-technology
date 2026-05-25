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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private static final int QUESTIONS_PER_SESSION = 5;
    private static final int MIN_QUESTIONS_TO_START = 3;

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

    /** Creates a session with given players and snapshots question IDs. */
    public Session createSession(Long categoryId, List<String> players) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return null;

        List<Question> active = questionRepository.findByCategoryIdAndActive(categoryId, true);
        if (active.size() < MIN_QUESTIONS_TO_START) return null;

        Collections.shuffle(active);
        List<Long> questionIds = active.stream()
                .limit(QUESTIONS_PER_SESSION)
                .map(Question::getId)
                .collect(Collectors.toList());

        List<String> cleanPlayers = (players == null) ? new ArrayList<>() :
                players.stream()
                        .filter(p -> p != null && !p.trim().isEmpty())
                        .map(String::trim)
                        .collect(Collectors.toList());

        return sessionRepository.save(
                Session.builder()
                        .category(category)
                        .startedAt(LocalDateTime.now())
                        .completed(false)
                        .players(cleanPlayers)
                        .questionIds(questionIds)
                        .build());
    }

    /** Legacy single-arg version kept for the REST controller. */
    public Session createSession(Long categoryId) {
        return createSession(categoryId, new ArrayList<>());
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

    /** Returns the questions belonging to this session, in stored order. */
    public List<Question> getQuestionsForSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) return Collections.emptyList();

        List<Long> ids = session.getQuestionIds();
        if (ids != null && !ids.isEmpty()) {
            return ids.stream()
                    .map(id -> questionRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // legacy fallback: no snapshot — derive from category
        if (session.getCategory() == null) return Collections.emptyList();
        List<Question> all = questionRepository.findByCategoryIdAndActive(
                session.getCategory().getId(), true);
        return all.stream().limit(QUESTIONS_PER_SESSION).collect(Collectors.toList());
    }

    /** Kept for the REST controller signature compatibility. */
    public List<Question> getQuestionsForSession(Long sessionId, int limit) {
        return getQuestionsForSession(sessionId);
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