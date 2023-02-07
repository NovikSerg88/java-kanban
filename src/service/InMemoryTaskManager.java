package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int setId() {
        return id++;
    }

    /*Получение списка всех задач, подзадач, епиков*/
    @Override
    public HashMap<Integer, Task> getListOfTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getListOfSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getListOfEpics() {
        return epics;
    }

    /*Удаление всех задач, подзадач, епиков*/
    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).getSubtasks().clear();
            updateEpicsStatus(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    /*Получение по идентификатору*/
    @Override
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getEpic(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    /*Добавление задачи, подзадачи, епика*/
    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        subtasks.put(subtask.getId(), subtask);
        epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
        updateEpicsStatus(epicId);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /*Обновление задачи, подзадачи, епика*/
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        subtasks.put(id, subtask);
        epics.get(epicId).getSubtasks().put(id, subtask);
        updateEpicsStatus(epicId);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.put(id, epic);
    }

    /*Удаление по идентификатору*/
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicId).getSubtasks().remove(id);
        updateEpicsStatus(epicId);
    }

    @Override
    public void deleteEpic(int id) {
        for (int subtaskId : epics.get(id).getSubtasks().keySet()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    /*Обновление статуса эпика*/
    @Override
    public void updateEpicsStatus(int epicId) {
        Epic epic = epics.get(epicId);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();

        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;
        for (Integer subtaskId : subtasks.keySet()) {
            if (subtasks.get(subtaskId).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtasks.get(subtaskId).getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if (!allNew && !allDone) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (allNew) {
            epic.setStatus(Status.NEW);
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}



