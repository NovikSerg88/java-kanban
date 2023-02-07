package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    int setId();

    /*Получение списка всех задач, подзадач, епиков*/
    HashMap<Integer, Task> getListOfTasks();

    HashMap<Integer, Subtask> getListOfSubtasks();

    HashMap<Integer, Epic> getListOfEpics();

    /*Удаление всех задач, подзадач, епиков*/
    public void deleteAllTask();

    void deleteAllSubtasks();

    void deleteAllEpics();

    /*Получение по идентификатору*/
    Task getTask(int id);

    Subtask getSubtask(int id);

    Task getEpic(int id);

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

    /*Обновление статуса эпика*/
    void updateEpicsStatus(int epicId);

    List<Task> getHistory();

}



