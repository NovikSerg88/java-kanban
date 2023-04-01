package service;

import exception.OverlapTaskException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

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
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    /*Добавление задачи, подзадачи, эпика*/
    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        checkTaskOverlap();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        subtasks.put(subtask.getId(), subtask);
        epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
        updateEpicsStatus(epicId);
        getEpicTime(epicId);
        checkTaskOverlap();
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
        checkTaskOverlap();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        subtasks.put(id, subtask);
        epics.get(epicId).getSubtasks().put(id, subtask);
        updateEpicsStatus(epicId);
        checkTaskOverlap();
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
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    /*Обновление статуса эпика*/
    public void updateEpicsStatus(int epicId) {
        Epic currentEpic = epics.get(epicId);


        if (subtasks.isEmpty()) {
            currentEpic.setStatus(Status.NEW);
            return;
        }
        Map<Integer, Subtask> subtasksOfCurrentEpic = currentEpic.getSubtasks();
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

    @Override
    public void getEpicTime(int epicId) {
        int duration = 0;
        LocalDateTime maxStartTime = LocalDateTime.MAX;
        LocalDateTime minEndTime = LocalDateTime.MIN;

        Epic epic = epics.get(epicId);
        Map<Integer, Subtask> epicSubtasks = epic.getSubtasks();

        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(0);
            epic.setEndTime(null);
            return;
        }
        //вычисление epicEndTime, epicStartTime, duration
        for (Integer id : epicSubtasks.keySet()) {
            if (epicSubtasks.get(id).getStartTime() != null && epicSubtasks.get(id).getStartTime().isBefore(maxStartTime)) {
                maxStartTime = epicSubtasks.get(id).getStartTime();
            }
            if (epicSubtasks.get(id).getStartTime() != null && epicSubtasks.get(id).getStartTime().isAfter(minEndTime)) {
                minEndTime = epicSubtasks.get(id).getStartTime();
            }
            duration += epicSubtasks.get(id).getDuration();

        }
        epic.setEndTime(minEndTime);
        epic.setStartTime(maxStartTime);
        epic.setDuration(duration);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> sortedTasks = new TreeSet<>((Task t1, Task t2) -> {
            if (t1.getStartTime() == null && t2.getStartTime() == null) {
                return 0;
            } else if (t1.getStartTime() == null) {
                return 1;
            } else if (t2.getStartTime() == null) {
                return -1;
            } else {
                return t1.getStartTime().compareTo(t2.getStartTime());
            }
        });
        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(subtasks.values());
        return new ArrayList<>(sortedTasks);
    }

    @Override
    public void checkTaskOverlap() throws OverlapTaskException {
        List<Task> tasks = getPrioritizedTasks();
        LocalDateTime endTime = null;
        for (Task task : tasks) {
            if (task.getStartTime() != null) {
                if (endTime != null && endTime.isAfter(task.getStartTime())) {
                    throw new OverlapTaskException("Обнаружено пересечение между " + task.getName() + " и предыдущей задачей");
                }
                endTime = task.getEndTime();
            }
        }
    }
}






