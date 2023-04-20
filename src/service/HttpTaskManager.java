package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVServer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final Type taskType = new TypeToken<List<Task>>() {
    }.getType();

    public HttpTaskManager(String url) {
        super();
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        System.out.println("Началась обработка метода save менеджера\n");
        try {
            String jsonTasks = gson.toJson(tasks.values());
            String jsonSubtasks = gson.toJson(subtasks.values());
            String jsonEpics = gson.toJson(epics.values());
            String jsonHistory = gson.toJson(historyManager.getHistory());

            kvTaskClient.put("tasks", jsonTasks);
            kvTaskClient.put("subtasks", jsonSubtasks);
            kvTaskClient.put("epics", jsonEpics);
            kvTaskClient.put("history", jsonHistory);

        } catch (RuntimeException e) {
            throw new ManagerSaveException("Не удалось сохранить данные по указанному ключу");
        }
    }

    public void load(String key) {
        System.out.println("Началась обработка метода load менеджера\n");
        try {

            String json = kvTaskClient.load(key);
            JsonElement jsonElement = JsonParser.parseString(json);

            switch (key) {
                case "tasks":
                    if (!jsonElement.isJsonArray()) {
                        System.out.println("Ответ от сервера не соответствует ожидаемому.");
                        return;
                    }

                    JsonArray tasksArray = jsonElement.getAsJsonArray();
                    for (JsonElement taskElement : tasksArray) {
                        Task task = gson.fromJson(taskElement, Task.class);
                        tasks.put(task.getId(), task);
                    }
                    break;
                case "subtasks":
                    if (!jsonElement.isJsonArray()) {
                        System.out.println("Ответ от сервера не соответствует ожидаемому.");
                        return;
                    }

                    JsonArray subtasksArray = jsonElement.getAsJsonArray();
                    for (JsonElement subtaskElement : subtasksArray) {
                        Subtask subtask = gson.fromJson(subtaskElement, Subtask.class);
                        subtasks.put(subtask.getId(), subtask);
                    }
                    break;
                case "epics":
                    if (!jsonElement.isJsonArray()) {
                        System.out.println("Ответ от сервера не соответствует ожидаемому.");
                        return;
                    }

                    JsonArray epicsArray = jsonElement.getAsJsonArray();
                    for (JsonElement epicElement : epicsArray) {
                        Epic epic = gson.fromJson(epicElement, Epic.class);
                        epics.put(epic.getId(), epic);
                    }
                    break;
                case "history":
                    if (!jsonElement.isJsonArray()) {
                        System.out.println("Ответ от сервера не соответствует ожидаемому.");
                        return;
                    }

                    JsonArray historyArray = jsonElement.getAsJsonArray();
                    List<Task> historyList = gson.fromJson(historyArray, taskType);
                    for (Task historyTask : historyList) {
                        historyManager.add(historyTask);
                    }
                    break;
                default:
                    System.out.println("Некорректный ключ.");
                    break;
            }
        } catch (RuntimeException e) {
            throw new ManagerSaveException("Не удалось получить данные по указанному ключу");
        }
    }
}
