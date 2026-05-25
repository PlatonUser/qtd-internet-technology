package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.model.SessionAnswer;
import ch.fhnw.qtd.service.CategoryService;
import ch.fhnw.qtd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/session")
public class SessionViewController {

    @Autowired private CategoryService categoryService;
    @Autowired private SessionService sessionService;

    // ----- SETUP -----
    @GetMapping("/setup")
    public String setup(@RequestParam Long categoryId,
                        Model model,
                        RedirectAttributes redir) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            redir.addFlashAttribute("error", "Category not found.");
            return "redirect:/";
        }
        model.addAttribute("category", category);
        return "session/setup";
    }

    @PostMapping("/start")
    public String start(@RequestParam Long categoryId,
                        @RequestParam(name = "players", required = false) List<String> players,
                        RedirectAttributes redir) {
        Session session = sessionService.createSession(categoryId, players);
        if (session == null) {
            redir.addFlashAttribute("error",
                "Could not start session. The category needs at least 3 active questions.");
            return "redirect:/session/setup?categoryId=" + categoryId;
        }
        return "redirect:/session/" + session.getId() + "/play?q=1";
    }

    // ----- PLAY -----
    @GetMapping("/{id}/play")
    public String play(@PathVariable Long id,
                       @RequestParam(name = "q", defaultValue = "1") int qIndex,
                       Model model,
                       RedirectAttributes redir) {
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            redir.addFlashAttribute("error", "Session not found.");
            return "redirect:/";
        }
        if (session.isCompleted()) {
            return "redirect:/session/" + id + "/summary";
        }

        List<Question> questions = sessionService.getQuestionsForSession(id);
        if (questions.isEmpty()) {
            redir.addFlashAttribute("error", "This session has no questions.");
            return "redirect:/";
        }

        int total = questions.size();
        if (qIndex < 1) qIndex = 1;
        if (qIndex > total) qIndex = total;

        Question current = questions.get(qIndex - 1);

        model.addAttribute("qtdSession", session);
        model.addAttribute("question", current);
        model.addAttribute("qIndex", qIndex);
        model.addAttribute("total", total);
        model.addAttribute("isLast", qIndex == total);
        model.addAttribute("players", session.getPlayers());
        return "session/play";
    }

    @PostMapping("/{id}/play")
    public String submitAnswers(@PathVariable Long id,
                                @RequestParam(name = "q") int qIndex,
                                @RequestParam Long questionId,
                                @RequestParam(name = "answers", required = false) List<String> answers,
                                RedirectAttributes redir) {
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            redir.addFlashAttribute("error", "Session not found.");
            return "redirect:/";
        }

        sessionService.saveAnswersForQuestion(id, questionId, session.getPlayers(), answers);

        int total = session.getQuestionIds().size();
        if (qIndex >= total) {
            sessionService.completeSession(id);
            return "redirect:/session/" + id + "/summary";
        }
        return "redirect:/session/" + id + "/play?q=" + (qIndex + 1);
    }

    // ----- SUMMARY -----
    @GetMapping("/{id}/summary")
    public String summary(@PathVariable Long id,
                          Model model,
                          RedirectAttributes redir) {
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            redir.addFlashAttribute("error", "Session not found.");
            return "redirect:/";
        }

        List<Question> questions = sessionService.getQuestionsForSession(id);
        List<SessionAnswer> answers = sessionService.getAnswersForSession(id);

        // Player list: from session, or fallback to distinct player names on answers (for legacy sessions)
        List<String> players = (session.getPlayers() != null && !session.getPlayers().isEmpty())
                ? new ArrayList<>(session.getPlayers())
                : answers.stream()
                        .map(SessionAnswer::getPlayerName)
                        .filter(p -> p != null && !p.isBlank())
                        .distinct()
                        .collect(Collectors.toList());

        // Build playerName -> (questionId -> answerText)
        Map<String, Map<Long, String>> byPlayer = new LinkedHashMap<>();
        for (String p : players) byPlayer.put(p, new LinkedHashMap<>());
        long answerCount = 0;
        for (SessionAnswer a : answers) {
            if (a.getPlayerName() == null) continue;
            byPlayer.computeIfAbsent(a.getPlayerName(), k -> new LinkedHashMap<>())
                    .put(a.getQuestion().getId(), a.getAnswerText());
            if (a.getAnswerText() != null && !a.getAnswerText().isBlank()) answerCount++;
        }

        // Flatten for template: Map<playerName, List<String>> where list is in question order
        Map<String, List<String>> playerAnswerList = new LinkedHashMap<>();
        for (String p : players) {
            List<String> list = new ArrayList<>();
            Map<Long, String> map = byPlayer.getOrDefault(p, new LinkedHashMap<>());
            for (Question q : questions) {
                String txt = map.get(q.getId());
                list.add(txt == null ? "" : txt);
            }
            playerAnswerList.put(p, list);
        }

        String formattedDate = session.getStartedAt()
                .format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        model.addAttribute("qtdSession", session);
        model.addAttribute("questions", questions);
        model.addAttribute("players", players);
        model.addAttribute("playerAnswerList", playerAnswerList);
        model.addAttribute("questionCount", questions.size());
        model.addAttribute("playerCount", players.size());
        model.addAttribute("answerCount", answerCount);
        model.addAttribute("startedAtFormatted", formattedDate);

        return "session/summary";
    }
}