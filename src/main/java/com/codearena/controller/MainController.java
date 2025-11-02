package com.codearena.controller;

import com.codearena.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        // If already logged in, redirect to dashboard
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        // If already logged in, redirect to dashboard
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("points", user.getPoints());
        model.addAttribute("completedProjects", user.getCompletedProjects());
        model.addAttribute("role", user.getRole());

        if ("ROLE_CLIENT".equals(user.getRole())) {
            return "dashboard-client";
        } else {
            return "dashboard";
        }
    }

    @GetMapping("/marketplace")
    public String marketplace(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "marketplace";
    }

    @GetMapping("/quests")
    public String quests(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "quests";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "leaderboard";
    }

    @GetMapping("/debug-session")
    @ResponseBody
    public String debugSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "Session active - User: " + user.getUsername() + " | Session ID: " + session.getId();
        } else {
            return "No active session | Session ID: " + session.getId();
        }
    }

    @GetMapping("/debug/templates")
    public String debugTemplates(Model model) {
        model.addAttribute("message", "Template test successful!");
        return "quests"; // This should resolve to quests.html
    }
}