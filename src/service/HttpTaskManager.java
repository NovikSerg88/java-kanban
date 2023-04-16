package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(String url) {
        super();
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        try {
            HashMap<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(tasks);
            allTasks.putAll(subtasks);
            allTasks.putAll(epics);

            String json = gson.toJson(allTasks);

            kvTaskClient.put("manager", json);

        } catch (RuntimeException e) {
            throw new RuntimeException("Запись не возможна.", e);
        }
    }

    public void load() {
        try {
            String json = kvTaskClient.load("manager");
            JsonElement jsonElement = JsonParser.parseString(json);

            if (!jsonElement.isJsonObject()) {
                System.out.println("Ответ от сервера не соответствует ожидаемому.");
                return;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonArray tasksArray = jsonObject.getAsJsonArray("tasks");
            Map<Integer, Task> tasks = new HashMap<>();
            for (JsonElement taskElement : tasksArray) {
                Task task = gson.fromJson(taskElement, Task.class);
                tasks.put(task.getId(), task);
            }

            JsonArray subtasksArray = jsonObject.getAsJsonArray("subtasks");
            Map<Integer, Subtask> subtasks = new HashMap<>();
            for (JsonElement subtaskElement : subtasksArray) {
                Subtask subtask = gson.fromJson(subtaskElement, Subtask.class);
                subtasks.put(subtask.getId(), subtask);
            }

            JsonArray epicsArray = jsonObject.getAsJsonArray("epics");
            Map<Integer, Epic> epics = new HashMap<>();
            for (JsonElement epicElement : epicsArray) {
                Epic epic = gson.fromJson(epicElement, Epic.class);
                epics.put(epic.getId(), epic);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
