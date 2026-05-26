package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Question;
import ch.fhnw.qtd.service.CategoryService;
import ch.fhnw.qtd.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/questions")
public class AdminQuestionsController {

    @Autowired private QuestionService questionService;
    @Autowired private CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        model.addAttribute("activeNav", "questions");
        return "admin/questions";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Question empty = new Question();
        empty.setActive(true);
        model.addAttribute("question", empty);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("formAction", "/admin/questions");
        model.addAttribute("formTitle", "Add Question");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("activeNav", "questions");
        return "admin/question-form";
    }

    @PostMapping
    public String create(@RequestParam String text,
                         @RequestParam Long categoryId,
                         @RequestParam(name = "active", required = false, defaultValue = "false") boolean active,
                         RedirectAttributes redir) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.length() < 10) {
            redir.addFlashAttribute("error", "Question text must be at least 10 characters.");
            return "redirect:/admin/questions/new";
        }
        Question q = questionService.createQuestion(trimmed, categoryId);
        if (q == null) {
            redir.addFlashAttribute("error", "Could not create question. Category not found.");
            return "redirect:/admin/questions/new";
        }
        // createQuestion defaults to active=true; honour the form value
        if (!active) {
            questionService.updateQuestion(q.getId(), null, null, false);
        }
        redir.addFlashAttribute("success", "Question created.");
        return "redirect:/admin/questions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redir) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            redir.addFlashAttribute("error", "Question not found.");
            return "redirect:/admin/questions";
        }
        model.addAttribute("question", question);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("formAction", "/admin/questions/" + id);
        model.addAttribute("formTitle", "Edit Question");
        model.addAttribute("submitLabel", "Save Changes");
        model.addAttribute("activeNav", "questions");
        return "admin/question-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String text,
                         @RequestParam Long categoryId,
                         @RequestParam(name = "active", required = false, defaultValue = "false") boolean active,
                         RedirectAttributes redir) {
        Question existing = questionService.getQuestionById(id);
        if (existing == null) {
            redir.addFlashAttribute("error", "Question not found.");
            return "redirect:/admin/questions";
        }
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.length() < 10) {
            redir.addFlashAttribute("error", "Question text must be at least 10 characters.");
            return "redirect:/admin/questions/" + id + "/edit";
        }
        questionService.updateQuestion(id, trimmed, categoryId, active);
        redir.addFlashAttribute("success", "Question updated.");
        return "redirect:/admin/questions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redir) {
        questionService.deleteQuestion(id);
        redir.addFlashAttribute("success", "Question deleted.");
        return "redirect:/admin/questions";
    }
}