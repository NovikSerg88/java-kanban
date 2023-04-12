package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.LocalDateTimeAdapter;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;

public class HttpTaskServer {

/*
public static void main(String[] args) throws IOException {
        HttpServer httpServer1 = HttpServer.create();
        httpServer1.bind(new InetSocketAddress(PORT), 0);
        httpServer1.createContext("/tasks/task", new taskHandler());
        httpServer1.createContext("/epics/epic", new epicHandler());
        httpServer1.createContext("/subtasks/subtask", new subtaskHandler());
        httpServer1.createContext("/tasks", new tasksHandler());
        httpServer1.createContext("/tasks/history", new tasksHistoryHandler());
        httpServer1.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
 */

    private static final TaskManager manager = Managers.getDefault();
    private static final int PORT = 8080;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    HttpServer httpServer = HttpServer.create();

    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new taskHandler());
        httpServer.createContext("/epics/epic", new epicHandler());
        httpServer.createContext("/subtasks/subtask", new subtaskHandler());
        httpServer.createContext("/tasks", new tasksHandler());
        httpServer.createContext("/tasks/history", new tasksHistoryHandler());
        httpServer.createContext("/tasks/task", new taskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    private void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    private void serverStop() {
        System.out.println("Останавливаем сервер на порту " + PORT);
        httpServer.stop(0);
    }

    static class taskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 200;
            String response = "";
            String method = exchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/task запроса от клиента.");
            switch (method) {
                case ("GET"):
                    String getQuery = exchange.getRequestURI().getQuery();
                    String[] getQueryParameter = getQuery.split("=");
                    int getId = Integer.parseInt(getQueryParameter[1]);
                    Task getTask = manager.getTask(getId);
                    response = gson.toJson(getTask);
                    break;
                case ("POST"):
                    Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class);
                    if (manager.getListOfTasks().containsKey(task.getId())) {
                        manager.updateTask(task);
                        response = "POST request: обновлена задача " + task.getName();
                    } else {
                        manager.addTask(task);
                        rCode = 201;
                        response = "POST request: добавлена задача " + task.getName();
                    }
                    break;
                case ("DELETE"):
                    String delQuery = exchange.getRequestURI().getQuery();
                    String[] delQueryParameter = delQuery.split("=");
                    int delId = Integer.parseInt(delQueryParameter[1]);
                    manager.deleteTask(delId);
                    response = "DELETE request: удалена задача id" + delId;
                    break;
                default:
                    rCode = 405;
                    response = "Некорректный метод запроса";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(rCode, 0);

            try (
                    OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class subtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 200;
            String response = "";
            String method = exchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /subtasks/subtask запроса от клиента.");
            switch (method) {
                case ("GET"):
                    String getQuery = exchange.getRequestURI().getQuery();
                    String[] getQueryParameter = getQuery.split("=");
                    int getId = Integer.parseInt(getQueryParameter[1]);
                    Subtask getSubtask = manager.getSubtask(getId);
                    response = gson.toJson(getSubtask);
                    break;
                case ("POST"):
                    Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class);
                    if (manager.getListOfSubtasks().containsKey(subtask.getId())) {
                        manager.updateSubtask(subtask);
                        response = "POST request: обновлена задача " + subtask.getName();
                    } else {
                        manager.addSubtask(subtask);
                        rCode = 201;
                        response = "POST request: добавлена задача " + subtask.getName();
                    }
                    break;
                case ("DELETE"):
                    String delQuery = exchange.getRequestURI().getQuery();
                    String[] delQueryParameter = delQuery.split("=");
                    int delId = Integer.parseInt(delQueryParameter[1]);
                    manager.deleteSubtask(delId);
                    response = "DELETE request: удалена подзадача id" + delId;
                    break;
                default:
                    rCode = 405;
                    response = "Некорректный метод запроса";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(rCode, 0);

            try (
                    OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class epicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 200;
            String response = "";
            String method = exchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /epics/epic запроса от клиента.");
            switch (method) {
                case ("GET"):
                    String getQuery = exchange.getRequestURI().getQuery();
                    String[] getQueryParameter = getQuery.split("=");
                    int getId = Integer.parseInt(getQueryParameter[1]);
                    Epic getEpic = manager.getEpic(getId);
                    response = gson.toJson(getEpic);
                    break;
                case ("POST"):
                    Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                    if (manager.getListOfEpics().containsKey(epic.getId())) {
                        manager.updateEpic(epic);
                        response = "POST request: обновлен эпик " + epic.getName();
                    } else {
                        manager.addEpic(epic);
                        rCode = 201;
                        response = "POST request: добавлен эпик " + epic.getName();
                    }
                    break;
                case ("DELETE"):
                    String delQuery = exchange.getRequestURI().getQuery();
                    String[] delQueryParameter = delQuery.split("=");
                    int delId = Integer.parseInt(delQueryParameter[1]);
                    manager.deleteEpic(delId);
                    response = "DELETE request: удален эпик id" + delId;
                    break;
                default:
                    rCode = 405;
                    response = "Некорректный метод запроса";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(rCode, 0);

            try (
                    OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class tasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 200;
            String response = "";
            String method = exchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");
            switch (method) {
                case ("GET"):
                    response = gson.toJson(manager.getPrioritizedTasks());
                    break;
                case ("DELETE"):
                    manager.deleteAllTask();
                    manager.deleteAllSubtasks();
                    manager.getPrioritizedTasks().clear();
                    response = "DELETE request: все задачи удалены";
                    break;
                default:
                    rCode = 405;
                    response = "Некорректный метод запроса";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(rCode, 0);

            try (
                    OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class tasksHistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 200;
            String response = "";
            String method = exchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/history запроса от клиента.");
            switch (method) {
                case ("GET"):
                    response = gson.toJson(manager.getHistory());
                    break;
                default:
                    rCode = 405;
                    response = "Некорректный метод запроса";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(rCode, 0);

            try (
                    OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
