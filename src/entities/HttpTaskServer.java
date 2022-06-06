package entities;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
import manager.FileBackedTasksManager;
import manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
/*    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
        new KVServer().start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }*/

    public static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента."); //убеждаемся, что хоть что-то работает

            //заготовка
            String path = httpExchange.getRequestURI().getPath();
            String method =  httpExchange.getRequestMethod();
            String[] pathSplit = path.split("/");
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String bodyStr = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            //
            RequestMapping requestMapping = new RequestMapping();
            Response response = requestMapping.response(method, pathSplit, query, bodyStr); /* получаем обработанный запрос из метода */

            httpExchange.sendResponseHeaders(response.code, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.response.getBytes());
            }
        }
    }
}