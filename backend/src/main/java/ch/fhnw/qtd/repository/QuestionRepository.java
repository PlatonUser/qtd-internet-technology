package ch.fhnw.qtd.repository;

import ch.fhnw.qtd.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategoryId(Long categoryId);
    List<Question> findByCategoryIdAndActive(Long categoryId, boolean active);
    List<Question> findByActive(boolean active);
    long countByCategoryIdAndActive(Long categoryId, boolean active);
}