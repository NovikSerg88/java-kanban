package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    public static int id = 0;
    public static HashMap<Integer, Task> tasks = new HashMap<>();
    public static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static HashMap<Integer, Epic> epics = new HashMap<>();

    public int setId() {
        return id++;
    }

    /*Получение списка всех задач, подзадач, епиков*/
    public HashMap<Integer, Task> getListOfTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getListOfSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getListOfEpics() {
        return epics;
    }

    /*Удаление всех задач, подзадач, епиков*/
    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    /*Получение по идентификатору*/
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Task getEpic(int id) {
        return epics.get(id);
    }

    /*Добавление задачи, подзадачи, епика*/
    public void addTask(Task task) {
        task.setId(setId());
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
//        Integer epicId = subtask.getEpicId();
        subtask.setId(setId());
        subtasks.put(subtask.getId(), subtask);
//        epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
    }

    public void addEpic(Epic epic) {
        epic.setId(setId());
        epics.put(epic.getId(), epic);
        epic.getSubtasks();
    }

    /*Обновление задачи, подзадачи, епика*/
    public void updateTask(int id, Task task, Status status) {
        task.setStatus(status);
        task.setId(id);
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(int id, Subtask subtask, Status status) {
        subtask.setStatus(status);
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(), epic);
    }

    /*Удаление по идентификатору*/
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    /*Получение списка всех подзадач эпика*/
    public HashMap<Integer, Subtask> getListOfSubtasks(int id) {
        return epics.get(id).getSubtasks();
    }

    /*Обновление статуса эпика*/
    public void updateEpicsStatus() {
        for (Integer id : epics.keySet()) {
            if (!epics.get(id).getSubtasks().isEmpty()) {
                boolean allDone = true;
                boolean allNew = true;
                for (Integer subtaskId : epics.get(id).getSubtasks().keySet()) {
                    if (epics.get(id).getSubtasks().get(subtaskId).getStatus() != Status.NEW) {
                        allNew = false;
                    }
                    if (epics.get(id).getSubtasks().get(subtaskId).getStatus() != Status.DONE) {
                        allDone = false;
                    }
                }
                if (!allNew & !allDone) {
                    epics.get(id).setStatus(Status.IN_PROGRESS);
                }
                if (allNew) {
                    epics.get(id).setStatus(Status.NEW);
                }
                if (allDone) {
                    epics.get(id).setStatus(Status.DONE);
                }
            } else {
                epics.get(id).setStatus(Status.NEW);
            }
        }
    }
}



