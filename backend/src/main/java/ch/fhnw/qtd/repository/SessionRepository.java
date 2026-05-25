package ch.fhnw.qtd.repository;

import ch.fhnw.qtd.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findAllByOrderByStartedAtDesc();
    long countByCategoryId(Long categoryId);
}