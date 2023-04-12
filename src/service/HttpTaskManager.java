package service;

import com.google.gson.Gson;
import exception.ManagerSaveException;
import model.Task;

import java.util.HashMap;

public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient kvTaskClient;

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

            Gson gson = new Gson();
            String json = gson.toJson(allTasks);

            kvTaskClient.put("tasks", json);

        } catch (RuntimeException e) {
            throw new RuntimeException("Запись не возможна.", e);
        }

    }
}
