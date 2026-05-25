package ch.fhnw.qtd.controller;

import ch.fhnw.qtd.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminViewController {

    @Autowired private AdminDashboardService dashboardService;

    @GetMapping("/admin/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("loginInfo", "You have been logged out.");
        }
        return "admin/login";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAllAttributes(dashboardService.getStats());
        model.addAttribute("breakdown", dashboardService.getCategoryBreakdown());
        model.addAttribute("activeNav", "dashboard");
        return "admin/dashboard";
    }
}