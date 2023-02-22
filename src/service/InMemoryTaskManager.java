package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int setId() {
        return id++;
    }

    /*Получение списка всех задач, подзадач, епиков*/
    @Override
    public Map<Integer, Task> getListOfTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Subtask> getListOfSubtasks() {
        return subtasks;
    }

    @Override
    public Map<Integer, Epic> getListOfEpics() {
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
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getEpic(int id) {
        historyManager.add(epics.get(id));
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
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicId).getSubtasks().remove(id);
        updateEpicsStatus(epicId);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int subtaskId : epics.get(id).getSubtasks().keySet()) {
            subtasks.remove(subtaskId);
            historyManager.remove(id);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    /*Обновление статуса эпика*/
    private void updateEpicsStatus(int epicId) {
        Epic currentEpic = epics.get(epicId);
        Map<Integer, Subtask> subtasksOfCurrentEpic = currentEpic.getSubtasks();

        if (subtasks.isEmpty()) {
            currentEpic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;
        for (Integer subtaskId : subtasksOfCurrentEpic.keySet()) {
            if (subtasksOfCurrentEpic.get(subtaskId).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtasksOfCurrentEpic.get(subtaskId).getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if (!allNew && !allDone) {
            currentEpic.setStatus(Status.IN_PROGRESS);
        }
        if (allNew) {
            currentEpic.setStatus(Status.NEW);
        }
        if (allDone) {
            currentEpic.setStatus(Status.DONE);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}



