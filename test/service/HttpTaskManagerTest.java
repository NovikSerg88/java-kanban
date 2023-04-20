package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exception.ManagerSaveException;
import model.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());

        client = new KVTaskClient(KV_URL);
        String tasks = client.load("tasks");
        String epics = client.load("epics");
        String subtasks = client.load("subtasks");
        String history = client.load("history");

        JsonElement jsonElement = JsonParser.parseString(tasks);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Task newTask = gson.fromJson(jsonArray.get(i), Task.class);
            assertEquals(newTask.getId(), task.getId());
        }

        jsonElement = JsonParser.parseString(epics);
        jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Epic newEpic = gson.fromJson(jsonArray.get(i), Epic.class);
            assertEquals(newEpic.getId(), epic.getId());
        }

        jsonElement = JsonParser.parseString(subtasks);
        jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Subtask newSubtask = gson.fromJson(jsonArray.get(i), Subtask.class);
            assertEquals(newSubtask.getId(), subtask.getId());
        }

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> newHistory = gson.fromJson(history, taskType);
        for (int i = 0; i < newHistory.size(); i++) {
            assertEquals(newHistory.get(i).getId(), taskManager.getHistory().get(i).getId());
        }
    }

    @Test
    public void loadTest() {

        LocalDateTime taskTime = LocalDateTime.of(2023, 3, 1, 1, 0);
        LocalDateTime subtaskTime = LocalDateTime.of(2023, 3, 1, 3, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, taskTime, 1);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, subtaskTime, 2, epic.getId());

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());

        HttpTaskManager newManager = new HttpTaskManager(KV_URL);
        newManager.load("tasks");
        newManager.load("epics");
        newManager.load("subtasks");
        newManager.load("history");

        for (int i = 0; i < newManager.getHistory().size(); i++) {
            assertEquals(newManager.getHistory().get(i).getId(), taskManager.getHistory().get(i).getId());
        }
        assertEquals(newManager.getTask(task.getId()).getId(), task.getId());
        assertEquals(newManager.getListOfSubtasks().size(), taskManager.getListOfSubtasks().size());
        assertFalse(newManager.getEpic(epic.getId()).getSubtasks().isEmpty());
    }
}


