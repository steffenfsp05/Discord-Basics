package org.pytenix.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.pytenix.model.ServerStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MinecraftApiService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://api.mcsrvstat.us/2/";


    public CompletableFuture<ServerStatus> checkServerStatusAsync(String serverIp) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + serverIp))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    JsonObject json = JsonParser.parseString(body).getAsJsonObject();
                    boolean online = json.get("online").getAsBoolean();

                    if (!online)
                        return new ServerStatus(serverIp, false, 0, 0, "N/A", null);


                    JsonObject players = json.getAsJsonObject("players");
                    int onlinePlayers = players.get("online").getAsInt();
                    int maxPlayers = players.get("max").getAsInt();
                    String version = json.get("version").getAsString();
                    String icon = json.has("icon") ? json.get("icon").getAsString() : null;

                    return new ServerStatus(serverIp, true, onlinePlayers, maxPlayers, version, icon);
                });
    }
}

