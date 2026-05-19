package org.pytenix.model;

public record ServerStatus(
        String ip,
        boolean isOnline,
        int onlinePlayers,
        int maxPlayers,
        String version,
        String iconBase64
) {}