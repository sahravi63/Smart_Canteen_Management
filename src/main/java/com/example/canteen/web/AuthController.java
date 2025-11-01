package com.example.canteen.web;

import com.example.canteen.model.User;
import com.example.canteen.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // GET custom login page
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // templates/signup.html
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return "signup";
        }
        userService.register(user); // expects that this encodes password and persists
        return "redirect:/login?registered";
    }
}
