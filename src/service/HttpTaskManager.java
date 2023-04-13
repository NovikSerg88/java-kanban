package service;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;

import java.time.LocalDateTime;
import java.util.HashMap;

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
}
