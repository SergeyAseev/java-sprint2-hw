package kv;

import manager.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    public HttpClient client;
    private String apiToken;
    private final String url;

    public KVTaskClient(int port) {
        this.client = HttpClient.newHttpClient();
        this.url = "http://localhost:" + port;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();

        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                this.apiToken = response.body();
            } else {
                System.out.println("Ошибка. Код состояния: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    // сохраняет состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
    public void put(String key, String json) throws IOException, InterruptedException {
        try {
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken)).
                    POST(body).
                    build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            System.out.println(response);
        } catch (Exception e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    // возвращает состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
    public String load(String key) {
        String answer = null;
        try {
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken)).
                    GET().
                    build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                answer = response.body();
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
        return answer;
    }
}