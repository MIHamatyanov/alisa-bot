package ru.kestar.alisabot.security;

import ru.kestar.alisabot.model.dto.ActiveUserInfo;

public class SecurityContextHelper {
    private static final ThreadLocal<ActiveUserInfo> activeUserContext = new ThreadLocal<>();

    private SecurityContextHelper() {}

    public static void setActiveUser(ActiveUserInfo activeUserInfo) {
        activeUserContext.set(activeUserInfo);
    }

    public static ActiveUserInfo getActiveUser() {
        return activeUserContext.get();
    }

    public static void clear() {
        activeUserContext.remove();
    }
}
