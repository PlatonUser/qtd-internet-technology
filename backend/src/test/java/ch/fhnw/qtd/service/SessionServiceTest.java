package ch.fhnw.qtd.service;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.repository.CategoryRepository;
import ch.fhnw.qtd.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SessionServiceTest {

    @Autowired private SessionService sessionService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private QuestionRepository questionRepository;

    @Test
    void cannotStartSessionWhenCategoryHasFewerThanThreeActiveQuestions() {
        Category cat = categoryRepository.save(Category.builder()
                .name("Test")
                .slug("test-" + System.nanoTime())
                .icon("\uD83E\uDDEA")
                .color("general")
                .active(true)
                .build());

        questionRepository.save(Question.builder()
                .text("Some question text one").category(cat).active(true).build());
        questionRepository.save(Question.builder()
                .text("Some question text two").category(cat).active(true).build());

        Session result = sessionService.createSession(cat.getId(), List.of("Alice", "Bob"));
        assertNull(result);
    }

    @Test
    void startsSessionWhenCategoryHasEnoughActiveQuestions() {
        Category cat = categoryRepository.save(Category.builder()
                .name("Test2")
                .slug("test2-" + System.nanoTime())
                .icon("\uD83E\uDDEA")
                .color("general")
                .active(true)
                .build());

        for (int i = 0; i < 5; i++) {
            questionRepository.save(Question.builder()
                    .text("Question number " + i + " is here")
                    .category(cat)
                    .active(true)
                    .build());
        }

        Session result = sessionService.createSession(cat.getId(), List.of("Alice", "Bob"));
        assertNotNull(result);
        assertEquals(2, result.getPlayers().size());
        assertFalse(result.getQuestionIds().isEmpty());
    }
}
