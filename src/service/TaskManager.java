package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    public static int id = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();

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
        for (Integer id : epics.keySet()) {
            epics.get(id).getSubtasks().clear();
            updateEpicsStatus(id);
        }
    }

    public void deleteAllEpics() {
        subtasks.clear();
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
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        subtasks.put(subtask.getId(), subtask);
        epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
        updateEpicsStatus(epicId);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /*Обновление задачи, подзадачи, епика*/
    public void updateTask(Task task) {
        int id = task.getId();
        tasks.put(id, task);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        subtasks.put(id, subtask);
        epics.get(epicId).getSubtasks().put(id, subtask);
        updateEpicsStatus(epicId);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.put(id, epic);
    }

    /*Удаление по идентификатору*/
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubtask(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicId).getSubtasks().remove(id);
        updateEpicsStatus(epicId);
    }

    public void deleteEpic(int id) {
        for (int subtaskId : epics.get(id).getSubtasks().keySet()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    /*Обновление статуса эпика*/
    private void updateEpicsStatus(int epicId) {
        if (!epics.get(epicId).getSubtasks().isEmpty()) {
            boolean allDone = true;
            boolean allNew = true;
            for (Integer subtaskId : epics.get(epicId).getSubtasks().keySet()) {
                if (epics.get(epicId).getSubtasks().get(subtaskId).getStatus() != Status.NEW) {
                    allNew = false;
                }
                if (epics.get(epicId).getSubtasks().get(subtaskId).getStatus() != Status.DONE) {
                    allDone = false;
                }
            }
            if (!allNew && !allDone) {
                epics.get(epicId).setStatus(Status.IN_PROGRESS);
            }
            if (allNew) {
                epics.get(epicId).setStatus(Status.NEW);
            }
            if (allDone) {
                epics.get(epicId).setStatus(Status.DONE);
            }
        } else {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }
}



