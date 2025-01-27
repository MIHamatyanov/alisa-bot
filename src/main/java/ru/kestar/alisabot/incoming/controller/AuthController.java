package ru.kestar.alisabot.incoming.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kestar.alisabot.config.properties.TelegramBotProperties;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final TelegramBotProperties botProperties;

    @GetMapping("/auth/success")
    public String authSuccess(Model model) {
        model.addAttribute("botUsername", botProperties.getUsername());
        return "auth-success";
    }
}
