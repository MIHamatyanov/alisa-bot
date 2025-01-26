package ru.kestar.alisabot.incoming.bot;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.incoming.bot.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class TelegramHandlersRegisterer {
    private final List<UpdateHandler> updateHandlers;
    private final TelegramRequestDispatcher dispatcher;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        updateHandlers.forEach(handler -> {
            handler.getSupportedCommands().forEach(command ->
                dispatcher.registerCommandHandler(command, handler)
            );
            handler.getSupportedCallbacks().forEach(callback ->
                dispatcher.registerCallbackHandler(callback, handler)
            );
        });
    }
}
