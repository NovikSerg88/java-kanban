package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;
    private static final String PATH = "resources/Data.csv";

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager() {
        super();
    }

    public static void main(String[] args) {
        Path path = Path.of(PATH);
        File file = new File(String.valueOf(path));
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        LocalDateTime time1 = LocalDateTime.of(2023, 03, 1, 17, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 2, 17, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 3, 19, 00);
        LocalDateTime time4 = LocalDateTime.of(2023, 03, 4, 19, 00);
        LocalDateTime time5 = LocalDateTime.of(2023, 03, 5, 21, 00);

        Task task1 = new Task(fileBackedTaskManager.setId(), "Task1", "", Status.NEW, time1, 2);
        Task task2 = new Task(fileBackedTaskManager.setId(), "Task2", "", Status.NEW, time2, 2);
        Epic epic1 = new Epic(fileBackedTaskManager.setId(), "Epic1", "", Status.NEW, time3, 2);
        Subtask subtask1 = new Subtask(fileBackedTaskManager.setId(), "Sub1", "", Status.DONE, time4, 2, 2);
        Subtask subtask2 = new Subtask(fileBackedTaskManager.setId(), "Sub2", "", Status.NEW, time5, 2, 2);

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);


        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getEpic(2);
        fileBackedTaskManager.getSubtask(3);
        fileBackedTaskManager.getTask(0);

        /* Создаем новый менеджер из файла */
        TaskManager fileBackedTaskManager1 = loadFromFile(file);

        /* Проверяем, что все таски и история просмотра восстановлены из файла */
        System.out.println(fileBackedTaskManager1.getListOfTasks());
        System.out.println(fileBackedTaskManager1.getListOfSubtasks());
        System.out.println(fileBackedTaskManager1.getListOfEpics());
        System.out.println(fileBackedTaskManager1.getHistory());
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
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
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
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicsStatus(int epicId) {
        super.updateEpicsStatus(epicId);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void getEpicTime(int epicId) {
        super.getEpicTime(epicId);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public boolean checkTaskOverlap(Task newTask) {
        return super.checkTaskOverlap(newTask);
    }


    /* Метод сохранения менеджера в файл */
    public void save() {
        try {
            Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
            HashMap<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(tasks);
            allTasks.putAll(subtasks);
            allTasks.putAll(epics);
            fileWriter.write("id,type,name,status,description,epic, startTime, duration\n");
            for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) {
                fileWriter.write(entry.getValue().toString() + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }

    /* Метод восстановления данных менеджера из файла в память */
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        List<Integer> history;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            //Пропустили титул
            br.readLine();
            //Читаем файл дальше пока строка не пустая
            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                //Создаем таску в менеджере из прочитанной строки и формируем коллекции в памяти
                Task task = manager.fromString(line);
                if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    manager.epics.put(epic.getId(), epic);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    int epicId = subtask.getEpicId();
                    manager.subtasks.put(subtask.getId(), subtask);
                    manager.epics.get(epicId).getSubtasks().put(subtask.getId(), subtask);
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
            //Читаем строку с историей и формируем менеджер истории
            history = historyFromString(br.readLine());
            for (int id : history) {
                if (manager.tasks.get(id) != null) {
                    manager.historyManager.add(manager.tasks.get(id));
                } else if (manager.subtasks.get(id) != null) {
                    manager.historyManager.add(manager.subtasks.get(id));
                } else if (manager.epics.get(id) != null) {
                    manager.historyManager.add(manager.epics.get(id));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
            return null;
        }
        return manager;
    }

    /* Метод создания задачи из строки */
    public Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Task task = null;
        String[] line = value.split("\\n");
        for (int i = 0; i < line.length; i++) {
            String[] part = line[i].split(",");
            int id = Integer.parseInt(part[0]);
            TaskType taskType = TaskType.valueOf(part[1]);
            String name = part[2];
            Status status = Status.valueOf(part[3]);
            String description = part[4];
            LocalDateTime startTime = LocalDateTime.parse(part[5], formatter);
            int duration = Integer.parseInt(part[6]);
            switch (taskType) {
                case TASK:
                    task = new Task(id, name, description, status, startTime, duration);
                    break;
                case SUBTASK:
                    int epicId = Integer.parseInt(part[7]);
                    task = new Subtask(id, name, description, status, startTime, duration, epicId);
                    break;
                case EPIC:
                    task = new Epic(id, name, description, status, startTime, duration);
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестный тип задачи");
            }
        }
        return task;
    }

    /* Метод сохранения истории в строку */
    public static String historyToString(HistoryManager manager) {
        List<Integer> id = new ArrayList<>();
        if (manager.getHistory() == null) {
            return "";
        }
        for (Task task : manager.getHistory()) {
            if(task != null) {
                id.add(task.getId());
            }
        }
        // Преобразовали список id в строку и убрали []
        String history = id.toString();
        history = history.substring(1, history.length() - 1);
        return history;
    }

    /* Метод восстановления id просмотренных тасок менеджера истории из строки */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            String[] line = value.split(", ");
            for (int i = 0; i < line.length; i++) {
                history.add(Integer.valueOf(line[i]));
            }
        }
        return history;
    }
}








