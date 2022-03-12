package ru.job4j.accident.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accident.model.User;
import ru.job4j.accident.service.AccidentService;

@Controller
public class RegControl {
    private final PasswordEncoder encoder;
    private AccidentService service;

    public RegControl(PasswordEncoder encoder, AccidentService service) {
        this.encoder = encoder;
        this.service = service;
    }

    @PostMapping("/reg")
    public String regSave(@ModelAttribute User user, Model model) {
        var username = service.findUserByUsername(user.getUsername());
        if (username != null) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует, "
                    + "придумайте другой username");
            return "reg";
        } else {
            user.setEnabled(true);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setAuthority(service.findByAuthority("ROLE_USER"));
            service.add(user);
            return "redirect:/login?reg=true";
        }

    }

    @GetMapping("/reg")
    public String regPage() {
        return "reg";
    }
}
