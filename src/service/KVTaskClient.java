package service;

import exception.ManagerSaveException;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String serverUrl;
    private final String apiToken;

    public KVTaskClient(String serverUrl) {
        this.serverUrl = serverUrl;
        apiToken = register();
    }

    public void put(String key, String json) {
        String url = "";
        switch (key) {
            case("tasks") :
                url = serverUrl + "/save/" + "tasks" + "?API_TOKEN=" + apiToken;
                break;
            case("subtasks") :
                url = serverUrl + "/save/" + "subtasks" + "?API_TOKEN=" + apiToken;
                break;
            case("epics"):
                url = serverUrl + "/save/" + "epics" + "?API_TOKEN=" + apiToken;
                break;
            case("history"):
                url = serverUrl + "/save/" + "history" + "?API_TOKEN=" + apiToken;
                break;
            default:
                System.out.println("Введен не существующий ключ");
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("put response: " + response.body());
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Во время выполнения запроса по адресу" + url + "возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        String url = serverUrl + "/load/" + key + "?API_TOKEN=" + apiToken;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("load response: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Во время выполнения запроса по адресу" + url + "возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    private String register() {
        String url = serverUrl + "/register";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("register response: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Во время выполнения запроса по адресу" + url + "возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}
