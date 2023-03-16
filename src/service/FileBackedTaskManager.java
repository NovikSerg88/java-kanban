package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File dir = new File("C:\\Users\\Sergei\\dev\\java-kanban\\resources");
    File file = new File(dir, "Tasks.csv");

    public static void main(String[] args) throws IOException {
        File dir = new File("C:\\Users\\Sergei\\dev\\java-kanban\\resources");
        File file = new File(dir, "Tasks.csv");
        TaskManager fileBackedTaskManager = Managers.getFileBacked();

        Task task1 = new Task(fileBackedTaskManager.setId(), "Task1", "DescriptionTask1", Status.NEW);
        Task task2 = new Task(fileBackedTaskManager.setId(), "Task2", "DescriptionTask2", Status.NEW);
        Epic epic1 = new Epic(fileBackedTaskManager.setId(), "Epic1", "DescriptionEpic1", Status.NEW);
        Subtask subtask1 = new Subtask(fileBackedTaskManager.setId(), "Subtask1", "DescriptionSubtask1", Status.NEW, 2);
        Subtask subtask2 = new Subtask(fileBackedTaskManager.setId(), "Subtask2", "DescriptionSubtask2", Status.NEW, 2);
        Epic epic2 = new Epic(fileBackedTaskManager.setId(), "Epic2", "DescriptionEpic2", Status.NEW);

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);
        fileBackedTaskManager.addEpic(epic2);

        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getEpic(2);
        fileBackedTaskManager.getSubtask(3);
        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getEpic(5);

        loadFromFile(file);
        List<Task> history = fileBackedTaskManager.getHistory();
        System.out.println(history);

    }

    @Override
    public int setId() {
        return super.setId();
    }

    @Override
    public Map<Integer, Task> getListOfTasks() {
        return super.getListOfTasks();
    }

    @Override
    public Map<Integer, Subtask> getListOfSubtasks() {
        return super.getListOfSubtasks();
    }

    @Override
    public Map<Integer, Epic> getListOfEpics() {
        return super.getListOfEpics();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Override
    public Task getTask(int id) {
        super.getTask(id);
        save();
        return super.getTask(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        super.getSubtask(id);
        save();
        return super.getSubtask(id);
    }

    @Override
    public Task getEpic(int id) {
        super.getEpic(id);
        save();
        return super.getEpic(id);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public void save() {
        try {
            Writer fileWriter = new FileWriter(file.getPath(), StandardCharsets.UTF_8);
            HashMap<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(tasks);
            allTasks.putAll(subtasks);
            allTasks.putAll(epics);
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) {
                fileWriter.write(entry.getValue().toString() + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна!", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
            String str = Files.readString(Path.of(file.toURI()));
            List<Integer> historyId = new ArrayList<>();
            fileBackedTaskManager.fromFile(str);
            historyId = historyFromString(str);
            for (int id : historyId) {
                if (fileBackedTaskManager.tasks.get(id) != null) {
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.tasks.get(id));
                } else if (fileBackedTaskManager.subtasks.get(id) != null) {
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.subtasks.get(id));
                } else if (fileBackedTaskManager.epics.get(id) != null) {
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.epics.get(id));
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
            return null;
        }
    }

    public void fromFile(String value) {
        String[] split = value.split("\\n");
        String[] tasks = new String[0];
        String taskString = new String();
        for (int i = 1; i < split.length - 2; i++) {
            tasks = split[i].split(",");
            taskString = String.join(",", tasks);
            Task task = fromString(taskString);
            //           getMap(task);
        }
    }

    public void getMap(Task task) {
        if (task.getType().equals(Type.TASK)) {
            addTask(task);
        }
        if (task.getType().equals(Type.SUBTASK)) {
            addSubtask((Subtask) task);
        }
        if (task.getType().equals(Type.EPIC)) {
            addEpic((Epic) task);
        }
    }

    public Task fromString(String value) {
        Task task = null;
        String[] split = value.split("\\n");
        for (int i = 0; i < split.length; i++) {
            String[] array = split[i].split(",");
            int id = Integer.parseInt(array[0]);
            Type type = Type.valueOf(array[1]);
            String name = array[2];
            Status status = Status.valueOf(array[3]);
            String dscr = array[4];
            if (type == Type.TASK) {
                task = new Task(id, name, dscr, status);
            }
            if (type == Type.SUBTASK) {
                task = new Subtask(id, name, dscr, status, Integer.parseInt(array[5]));
            }
            if (type == Type.EPIC) {
                task = new Epic(id, name, dscr, status);
            }
        }
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        List<Integer> historyId = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyId.add(task.getId());
        }
        String history = historyId.toString();
        history = history.substring(1, history.length() - 1);
        return history;
    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split("\\n");
        String[] lines = new String[0];
        List<Integer> idList = new ArrayList<>();
        for (int i = split.length - 1; i > split.length - 2; i--) {
            lines = split[i].split(",");
            for (int j = 0; j < lines.length; j++) {
                idList.add(j);
            }
        }
        return idList;
    }
}







