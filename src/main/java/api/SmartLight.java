package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import javax.json.Json;

import io.helidon.config.Config;

public class SmartLight {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    private String id;
    private String alias;
    private URI uri;

    public SmartLight(String id, String alias, Config config) {
        this.id = id;
        this.alias = alias;

        String host = config.get("hue.bridge.host").asString()
                .orElseThrow(() -> new RuntimeException("bridge.host not set"));
        String token = config.get("hue.bridge.token").asString()
                .orElseThrow(() -> new RuntimeException("bridge.token not set"));

        this.uri = URI.create("http://" + host + "/api/" + token + "/lights/" + id + "/state");
    }

    public boolean on() {
        return sendRequest(true);
    }

    public boolean off() {
        return sendRequest(false);
    }

    private boolean sendRequest(boolean on) {
        try {
            String json = Json.createObjectBuilder().add("on", on).build().toString();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).PUT(BodyPublishers.ofString(json)).build();
            return HTTP_CLIENT.send(request, BodyHandlers.ofString()).statusCode() == 200;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
