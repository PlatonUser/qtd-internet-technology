package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.model.Category;
import ch.fhnw.qtd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories()
                .stream()
                .filter(Category::isActive)
                .toList();
        model.addAttribute("categories", categories);
        return "home";
    }
}