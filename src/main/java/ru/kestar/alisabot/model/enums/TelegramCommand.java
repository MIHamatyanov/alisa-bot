package ru.kestar.alisabot.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TelegramCommand {
    START("/start");

    private final String command;

    private static final TelegramCommand[] VALUES = values();

    public static TelegramCommand fromString(String commandString) {
        for (TelegramCommand command : VALUES) {
            if (command.getCommand().equals(commandString)) {
                return command;
            }
        }
        return null;
    }
}
