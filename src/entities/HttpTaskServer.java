package entities;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    public static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента."); //убеждаемся, что хоть что-то работает

            //заготовка
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            String[] pathSplit = path.split("/");
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String bodyStr = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            //
            RequestMapping requestMapping = new RequestMapping();
            Response response = requestMapping.response(method, pathSplit, query, bodyStr);

            httpExchange.sendResponseHeaders(response.code, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.response.getBytes());
            }
        }
    }
}