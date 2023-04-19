package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static KVServer kvserver;
    private static KVTaskClient client;
    private static final String KV_URL = "http://localhost:8078";
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

    @BeforeEach
    public void setUp() {
        taskManager = new HttpTaskManager(KV_URL);
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        kvserver = new KVServer();
        kvserver.start();
        new HttpTaskServer();
    }

    @Test
    public void saveTest() {
        LocalDateTime taskTime = LocalDateTime.of(2023, 3, 1, 1, 0);
        LocalDateTime subtaskTime = LocalDateTime.of(2023, 3, 1, 3, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, taskTime, 1);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, subtaskTime, 2, epic.getId());

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addTask(task);
        taskManager.save();


        client = new KVTaskClient(KV_URL);
        String jsonString = client.load("tasks");
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject id = jsonObject.get("id").getAsJsonObject();
        System.out.println(id);




    }
}


