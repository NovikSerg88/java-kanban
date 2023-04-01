package service;

import exception.OverlapTaskException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    /*Добавление задачи, подзадачи, епика*/
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    /*Обновление задачи, подзадачи, епика*/
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    /*Удаление по идентификатору*/
    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    List<Task> getHistory();

    void getEpicTime(int id);

    List<Task> getPrioritizedTasks();

    void checkTaskOverlap();

}



