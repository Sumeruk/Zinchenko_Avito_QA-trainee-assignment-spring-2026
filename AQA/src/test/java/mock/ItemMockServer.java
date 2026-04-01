package mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ItemMockServer {

    private final HttpServer server;
    private final int port;
    private final ObjectMapper mapper = new ObjectMapper();

    public ItemMockServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/1/item", new ItemHandler());
        server.setExecutor(null); // Использует дефолтный executor
    }

    public void start() {
        server.start();
        System.out.println("Mock server started on port " + port);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Mock server stopped");
    }

    class ItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            JsonNode jsonNode = mapper.readTree(requestBody);

            ObjectNode responseNode = mapper.createObjectNode();


            if (jsonNode.isObject()) {
                jsonNode.fields().forEachRemaining(entry ->
                        responseNode.set(entry.getKey(), entry.getValue())
                );
            }

            responseNode.put("id", UUID.randomUUID().toString());

            String timestamp = ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS Z Z"));
            responseNode.put("createdAt", timestamp);

            // Отдаем ответ
            String responseString = mapper.writeValueAsString(responseNode);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseString.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseString.getBytes());
            }
        }
    }
}
