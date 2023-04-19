package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(String url) {
        super();
        this.kvTaskClient = new KVTaskClient(url);
 //       load();
    }

    @Override
    public void save() {
        System.out.println("Началась обработка метода save менеджера\n");
        try {
 //           Map<Integer, Task> allTasks = new HashMap<>();
 //           allTasks.putAll(tasks);
 //           allTasks.putAll(subtasks);
 //           allTasks.putAll(epics);
            String jsonTasks = gson.toJson(tasks);
            String jsonSubtasks = gson.toJson(subtasks);
            String jsonEpics = gson.toJson(epics);
            String jsonHistory = gson.toJson(historyManager);

            kvTaskClient.put("tasks", jsonTasks);
            kvTaskClient.put("subtasks", jsonSubtasks);
            kvTaskClient.put("epics", jsonEpics);
            kvTaskClient.put("history", jsonHistory);

        } catch (RuntimeException e) {
            throw new ManagerSaveException("Не удалось сохранить данные по указанному ключу");
        }
    }

    private void load() {
        System.out.println("Началась обработка метода load менеджера\n");
        try {
            String json = kvTaskClient.load("manager");
            JsonElement jsonElement = JsonParser.parseString(json);

            if (!jsonElement.isJsonObject()) {
                System.out.println("Ответ от сервера не соответствует ожидаемому.");
                return;
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray tasksArray = jsonObject.getAsJsonArray("tasks");
            for (JsonElement taskElement : tasksArray) {
                Task task = gson.fromJson(taskElement, Task.class);
                tasks.put(task.getId(), task);
            }
            JsonArray subtasksArray = jsonObject.getAsJsonArray("subtasks");
            for (JsonElement subtaskElement : subtasksArray) {
                Subtask subtask = gson.fromJson(subtaskElement, Subtask.class);
                subtasks.put(subtask.getId(), subtask);
            }
            JsonArray epicsArray = jsonObject.getAsJsonArray("epics");
            for (JsonElement epicElement : epicsArray) {
                Epic epic = gson.fromJson(epicElement, Epic.class);
                epics.put(epic.getId(), epic);
            }
        } catch (RuntimeException e) {
            throw new ManagerSaveException("Не удалось получить данные по указанному ключу");
        }
    }
}
