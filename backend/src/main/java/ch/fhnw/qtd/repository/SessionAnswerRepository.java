package ch.fhnw.qtd.repository;

import ch.fhnw.qtd.model.SessionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionAnswerRepository extends JpaRepository<SessionAnswer, Long> {
    List<SessionAnswer> findBySessionId(Long sessionId);
    long countBySessionId(Long sessionId);
}