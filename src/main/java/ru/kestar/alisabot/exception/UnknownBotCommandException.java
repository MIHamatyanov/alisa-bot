package ru.kestar.alisabot.exception;

public class UnknownBotCommandException extends RuntimeException {

    public UnknownBotCommandException() {
        super("Unknown command");
    }

}
