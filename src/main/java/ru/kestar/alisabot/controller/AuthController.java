package ru.kestar.alisabot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kestar.telegrambotstarter.bot.TelegramBot;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final TelegramBot bot;

    @GetMapping("/auth/success")
    public String authSuccess(Model model) {
        model.addAttribute("botUsername", bot.getBotUsername());
        return "auth-success";
    }
}
