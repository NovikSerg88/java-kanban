package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    int setId();

    /*Получение списка всех задач, подзадач, епиков*/
    Map<Integer, Task> getListOfTasks();

    Map<Integer, Subtask> getListOfSubtasks();

    Map<Integer, Epic> getListOfEpics();

    /*Удаление всех задач, подзадач, епиков*/
    public void deleteAllTask();

    void deleteAllSubtasks();

    void deleteAllEpics();

    /*Получение по идентификатору*/
    Task getTask(int id) throws IOException;

    Subtask getSubtask(int id) throws IOException;

    Task getEpic(int id) throws IOException;

    /*Добавление задачи, подзадачи, епика*/
    void addTask(Task task) throws IOException;

    void addSubtask(Subtask subtask) throws IOException;

    void addEpic(Epic epic) throws IOException;

    /*Обновление задачи, подзадачи, епика*/
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    /*Удаление по идентификатору*/
    void deleteTask(int id) throws IOException;

    void deleteSubtask(int id);

    void deleteEpic(int id);

    List<Task> getHistory();

}



