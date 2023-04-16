package service;
import model.Epic;
import model.Subtask;
import model.Task;

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
    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    /*Добавление задачи, подзадачи, епика*/
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    /*Обновление задачи, подзадачи, епика*/
    int updateTask(Task task);

    int updateSubtask(Subtask subtask);

    int updateEpic(Epic epic);

    /*Удаление по идентификатору*/
    int deleteTask(int id);

    int deleteSubtask(int id);

    int deleteEpic(int id);

    List<Task> getHistory();

    void getEpicTime(int id);

    List<Task> getPrioritizedTasks();

    boolean checkTaskOverlap(Task newTask);

}



