package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.repository.QuestionRepository;
import ch.fhnw.qtd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired private CategoryService categoryService;
    @Autowired private QuestionRepository questionRepository;

    @GetMapping
    public String list(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Category c : categories) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("category", c);
            row.put("activeQuestions", questionRepository.countByCategoryIdAndActive(c.getId(), true));
            rows.add(row);
        }
        model.addAttribute("rows", rows);
        model.addAttribute("activeNav", "categories");
        return "admin/categories";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Category empty = new Category();
        empty.setActive(true);
        empty.setIcon("\uD83D\uDCAC");  // 💬
        empty.setColor("general");
        model.addAttribute("category", empty);
        model.addAttribute("formAction", "/admin/categories");
        model.addAttribute("formTitle", "Add Category");
        model.addAttribute("submitLabel", "Create");
        model.addAttribute("activeNav", "categories");
        return "admin/category-form";
    }

    @PostMapping
    public String create(@ModelAttribute Category form, RedirectAttributes redir) {
        if (form.getName() == null || form.getName().isBlank()) {
            redir.addFlashAttribute("error", "Name is required.");
            return "redirect:/admin/categories/new";
        }
        if (form.getSlug() == null || form.getSlug().isBlank()) {
            form.setSlug(slugify(form.getName()));
        }
        if (form.getIcon() == null || form.getIcon().isBlank()) {
            form.setIcon("\uD83D\uDCAC");
        }
        if (form.getColor() == null || form.getColor().isBlank()) {
            form.setColor("general");
        }
        categoryService.save(form);
        redir.addFlashAttribute("success", "Category created.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redir) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            redir.addFlashAttribute("error", "Category not found.");
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("formAction", "/admin/categories/" + id);
        model.addAttribute("formTitle", "Edit Category");
        model.addAttribute("submitLabel", "Save Changes");
        model.addAttribute("activeNav", "categories");
        return "admin/category-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Category form,
                         RedirectAttributes redir) {
        Category existing = categoryService.getCategoryById(id);
        if (existing == null) {
            redir.addFlashAttribute("error", "Category not found.");
            return "redirect:/admin/categories";
        }
        if (form.getName() == null || form.getName().isBlank()) {
            redir.addFlashAttribute("error", "Name is required.");
            return "redirect:/admin/categories/" + id + "/edit";
        }
        existing.setName(form.getName());
        existing.setSlug((form.getSlug() == null || form.getSlug().isBlank())
                ? slugify(form.getName()) : form.getSlug());
        existing.setDescription(form.getDescription());
        existing.setIcon((form.getIcon() == null || form.getIcon().isBlank())
                ? "\uD83D\uDCAC" : form.getIcon());
        existing.setColor(form.getColor() == null ? "general" : form.getColor());
        existing.setActive(form.isActive());
        categoryService.save(existing);
        redir.addFlashAttribute("success", "Category updated.");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redir) {
        long active = questionRepository.countByCategoryIdAndActive(id, true);
        if (active > 0) {
            redir.addFlashAttribute("error",
                "Can't delete a category with active questions. Deactivate them first.");
            return "redirect:/admin/categories";
        }
        categoryService.deleteCategory(id);
        redir.addFlashAttribute("success", "Category deleted.");
        return "redirect:/admin/categories";
    }

    private String slugify(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }
}