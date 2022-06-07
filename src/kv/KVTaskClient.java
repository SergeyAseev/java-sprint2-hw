package kv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient client;
    private String apiToken;
    private URI uri;
    public KVTaskClient() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        apiToken = response.body();
        System.out.println("API_TOKEN: " + apiToken);
    }
    // сохраняет состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
    public void put(String key, String json) throws IOException, InterruptedException {
        try {
            uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
            client = HttpClient.newHttpClient();
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().
                            POST(body).
                            uri(uri).
                            build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            System.out.println(response);
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient - ошибка при сохранении данных");
        }
    }
    // возвращает состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
    public String load(String key) {
        String answer = null;
        try {
            uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
            client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().
                            GET().
                            uri(uri).
                            build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            answer = response.body();
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            System.out.println("KVTaskClient - ошибка при загрузке данных");
        }
        return answer;
    }
}