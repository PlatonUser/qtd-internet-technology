package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.model.Session;
import ch.fhnw.qtd.service.CategoryService;
import ch.fhnw.qtd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/session")
public class SessionViewController {

    @Autowired private CategoryService categoryService;
    @Autowired private SessionService sessionService;

    // GET /session/setup?categoryId=X  -> show "Who's Playing?" form
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

    // POST /session/start  -> create session, then redirect to play page
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
}