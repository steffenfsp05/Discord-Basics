package org.pytenix.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MinecraftApiService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://api.mcsrvstat.us/2/";


    public CompletableFuture<Boolean> checkServerStatusAsync(String serverIp) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + serverIp))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> body.contains("\"online\":true") || body.contains("\"online\": true"));
    }
}
