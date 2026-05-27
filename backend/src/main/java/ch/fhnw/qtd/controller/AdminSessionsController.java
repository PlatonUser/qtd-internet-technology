package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.repository.SessionAnswerRepository;
import ch.fhnw.qtd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/sessions")
public class AdminSessionsController {

    @Autowired private SessionService sessionService;
    @Autowired private SessionAnswerRepository sessionAnswerRepository;

    @GetMapping
    public String list(Model model) {
        List<Session> sessions = sessionService.getAllSessions();
        List<Map<String, Object>> rows = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMM yyyy");

        for (Session s : sessions) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("session", s);
            row.put("date", s.getStartedAt() != null ? s.getStartedAt().format(fmt) : "—");
            int qCount = (s.getQuestionIds() != null && !s.getQuestionIds().isEmpty())
                    ? s.getQuestionIds().size()
                    : sessionService.getQuestionsForSession(s.getId()).size();
            row.put("questionCount", qCount);
            row.put("answerCount", sessionAnswerRepository.countBySessionId(s.getId()));
            rows.add(row);
        }

        model.addAttribute("rows", rows);
        model.addAttribute("activeNav", "sessions");
        return "admin/sessions";
    }
}