package Service;

import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.util.HashMap;

public class TaskManager {
    public static int id = 0;
    public static HashMap<Integer, Task> tasks = new HashMap<>();
    public static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static HashMap<Integer, Epic> epics = new HashMap<>();

    public static int setId() {
        return id++;
    }

    /*Получение списка всех задач, подзадач, епиков*/
    public static HashMap<Integer, Task> getListOfTasks() {
        return tasks;
    }

    public static HashMap<Integer, Subtask> getListOfSubtasks() {
        return subtasks;
    }

    public static HashMap<Integer, Epic> getListOfEpics() {
        return epics;
    }

    /*Удаление всех задач, подзадач, епиков*/
    public static void deleteAllTask() {
        tasks.clear();
    }

    public static void deleteAllSubtasks() {
        subtasks.clear();
    }

    public static void deleteAllEpics() {
        epics.clear();
    }

    /*Получение по идентификатору*/
    public static Task getTask(int id) {
        return tasks.get(id);
    }

    public static Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public static Task getEpic(int id) {
        return epics.get(id);
    }

    /*Создание задачи, подзадачи, епика*/
    public static void createTask(Task task) {
        task.setId(setId());
        tasks.put(task.getId(), task);
    }

    public static void createSubtask(Subtask subtask) {
        subtask.setId(setId());
        subtasks.put(subtask.getId(), subtask);
    }

    public static void createEpic(Epic epic) {
        epic.setId(setId());
        epics.put(epic.getId(), epic);
        epic.getSubtasks();
    }

    /*Обновление задачи, подзадачи, епика*/
    public static void updateTask(int id, Task task) {
        task.setStatus(Task.Status.NEW);
        task.setId(id);
        tasks.put(task.getId(), task);
    }

    public static void updateSubtask(int id, Subtask subtask) {
        subtask.setStatus(Task.Status.NEW);
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
    }

    public static void updateEpic(int id, Epic epic) {
        epic.setStatus(Task.Status.NEW);
        epic.setId(id);
        epics.put(epic.getId(), epic);
    }

    /*Удаление по идентификатору*/
    public static void deleteTask(int id) {
        tasks.remove(id);
    }

    public static void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    public static void deleteEpic(int id) {
        epics.remove(id);
    }

    /*Получение списка всех подзадач эпика*/
    public static HashMap<Integer, Subtask> getListOfSubtasks(int id) {
        return epics.get(id).getSubtasks();
    }
}

