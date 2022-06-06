package entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
        // адрес регистрации
        uri = URI.create("http://localhost:8078/register");
        // запрос на регистрацию
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        // получаем токен, получаем ответ строкой
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        apiToken = response.body();
        System.out.println("\nKV-клиент запущен");
        System.out.println("API_TOKEN: " + apiToken);
    }
    // сохраняет состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
    public void save(String key, String json) throws IOException, InterruptedException {
        try {
            // создаём экземпляр URI, содержащий адрес нужного ресурса
            uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
            // HTTP-клиент с настройками по умолчанию
            client = HttpClient.newHttpClient();
            // создаём тело для запроса = полученный json
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            // создаём объект, описывающий HTTP-запрос
            HttpRequest request = HttpRequest.newBuilder().
                    POST(body). // указываем HTTP-метод запроса
                            uri(uri).   // указываем адрес ресурса
                            build();    // заканчиваем настройку и создаём ("строим") HTTP-запрос
            // получаем стандартный обработчик тела ответа с конвертацией содержимого в строку
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            // отправляем запрос и получаем ответ от сервера
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
            // создаём экземпляр URI, содержащий адрес нужного ресурса
            uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
            // HTTP-клиент с настройками по умолчанию
            client = HttpClient.newHttpClient();
            // создаём объект, описывающий HTTP-запрос
            HttpRequest request = HttpRequest.newBuilder().
                    GET(). // указываем HTTP-метод запроса
                            uri(uri).   // указываем адрес ресурса
                            build();    // заканчиваем настройку и создаём ("строим") HTTP-запрос
            // получаем стандартный обработчик тела ответа с конвертацией содержимого в строку
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            // отправляем запрос и получаем ответ от сервера
            HttpResponse<String> response = client.send(request, handler);
            answer = response.body();
            System.out.println(response);
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient - ошибка при загрузке данных");
        }
        return answer;
    }
}