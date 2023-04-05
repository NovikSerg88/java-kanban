package service;

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
    protected Set<Task> sortedTasks = new TreeSet<>(new StartTimeComparator());

    public class StartTimeComparator implements Comparator<Task> {

        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getStartTime() == null && t2.getStartTime() == null) {
                return 0;
            } else if (t1.getStartTime() == null) {
                return 1;
            } else if (t2.getStartTime() == null) {
                return -1;
            } else {
                return t1.getStartTime().compareTo(t2.getStartTime());
            }
        }
    }

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
        for (Integer taskId : tasks.keySet()) {
            sortedTasks.remove(tasks.get(taskId));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer epicId : epics.keySet()) {
            epics.get(epicId).getSubtasks().clear();
            updateEpicsStatus(epicId);
        }
        for (Integer subtaskId : subtasks.keySet()) {
            sortedTasks.remove(subtasks.get(subtaskId));
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
        boolean isOverlap = checkTaskOverlap(task);
        if (!isOverlap) {
            tasks.put(task.getId(), task);
            sortedTasks.add(task);
        } else {
            System.out.println("Невозможно добавить задачу " + task.getName() + ": задачи пересекаются по времени.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        boolean isOverlap = checkTaskOverlap(subtask);
        if (!isOverlap) {
            int epicId = subtask.getEpicId();
            subtasks.put(subtask.getId(), subtask);
            epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
            updateEpicsStatus(epicId);
            getEpicTime(epicId);
            sortedTasks.add(subtask);
        } else {
            System.out.println("Невозможно добавить задачу " + subtask.getName() + ": задачи пересекаются по времени.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /*Обновление задачи, подзадачи, епика*/
    @Override
    public void updateTask(Task task) {
        boolean isOverlap = checkTaskOverlap(task);
        if (!isOverlap) {
            System.out.println(sortedTasks);
            int currentId = task.getId();
            tasks.put(currentId, task);
            sortedTasks.add(task);
            System.out.println(sortedTasks);
        } else {
            System.out.println("Невозможно обновить задачу " + task.getName() + " : задачи пересекаются по времени.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        boolean isOverlap = checkTaskOverlap(subtask);
        if (!isOverlap) {
            int currentId = subtask.getId();
            int epicId = subtask.getEpicId();
            subtasks.put(currentId, subtask);
            epics.get(epicId).getSubtasks().put(currentId, subtask);
            updateEpicsStatus(epicId);
            sortedTasks.add(subtask);
        } else {
            System.out.println("Невозможно обновить задачу " + subtask.getName() + ": задачи пересекаются по времени.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int currentId = epic.getId();
        epics.put(currentId, epic);
    }

    /*Удаление по идентификатору*/
    @Override
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        historyManager.remove(id);
        sortedTasks.remove(task);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicId).getSubtasks().remove(id);
        updateEpicsStatus(epicId);
        historyManager.remove(id);
        sortedTasks.remove(subtask);
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
        for (Integer currentId : epicSubtasks.keySet()) {
            if (epicSubtasks.get(currentId).getStartTime() != null && epicSubtasks.get(currentId).getStartTime().isBefore(maxStartTime)) {
                maxStartTime = epicSubtasks.get(currentId).getStartTime();
            }
            if (epicSubtasks.get(currentId).getStartTime() != null && epicSubtasks.get(currentId).getStartTime().isAfter(minEndTime)) {
                minEndTime = epicSubtasks.get(currentId).getStartTime();
            }
            duration += epicSubtasks.get(currentId).getDuration();
        }
        epic.setEndTime(minEndTime);
        epic.setStartTime(maxStartTime);
        epic.setDuration(duration);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    @Override
    public boolean checkTaskOverlap(Task newTask) {
        List<Task> tasks = getPrioritizedTasks();
        boolean isOverlap = false;
        if (tasks.size() != 0) {
            for (Task task : tasks) {
                if (task.getStartTime() != null) {
                    if (newTask.getStartTime() != null && newTask.getEndTime() != null && newTask.getStartTime().isBefore(task.getEndTime())) {
                        isOverlap = true;
                    }
                }
            }
        }
        return isOverlap;
    }
}






