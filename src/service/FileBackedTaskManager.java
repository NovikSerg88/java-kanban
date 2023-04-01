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
    private final File file;
    static final String PATH = "resources/Data.csv";

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        Path path = Path.of(PATH);
        File file = new File(String.valueOf(path));
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        Task task1 = new Task(fileBackedTaskManager.setId(), "Task1", "", Status.NEW);
        Task task2 = new Task(fileBackedTaskManager.setId(), "Task2", "", Status.NEW);
        Epic epic1 = new Epic(fileBackedTaskManager.setId(), "Epic1", "", Status.NEW);
        Subtask subtask1 = new Subtask(fileBackedTaskManager.setId(), "Sub1", "", Status.DONE, 2);
        Subtask subtask2 = new Subtask(fileBackedTaskManager.setId(), "Sub2", "", Status.NEW, 2);
        Subtask subtask3 = new Subtask(fileBackedTaskManager.setId(), "Sub3", "", Status.NEW, 2);


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time1 = LocalDateTime.of(2023, 03, 30, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 30, 17, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 30, 19, 00);
        LocalDateTime time4 = LocalDateTime.of(2023, 03, 31, 19, 00);
        LocalDateTime time5 = LocalDateTime.of(2023, 03, 31, 21, 00);

        subtask1.setDuration(2);
        subtask1.setStartTime(time1);

        subtask2.setDuration(2);
        subtask2.setStartTime(time2);

        subtask3.setDuration(1);
        subtask3.setStartTime(time3);

        task1.setStartTime(time4);
        task2.setStartTime(time5);

        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);
        fileBackedTaskManager.addSubtask(subtask3);
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);

        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getEpic(2);
        fileBackedTaskManager.getSubtask(3);
        fileBackedTaskManager.getTask(0);
        fileBackedTaskManager.getTask(1);

        /* Создаем новый менеджер из файла */
        TaskManager fileBackedTaskManager1 = loadFromFile(file);

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
    public List<Task> getHistory() {
        return super.getHistory();
    }

    /* Добавили три метода добавления тасок в память*/
    public void addTaskInMemory(Task task) {
        super.addTask(task);
    }

    public void addSubtaskInMemory(Subtask subtask) {
        super.addSubtask(subtask);
    }

    public void addEpicInMemory(Epic epic) {
        super.addEpic(epic);
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
                    manager.addEpicInMemory((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtaskInMemory((Subtask) task);
                } else {
                    manager.addTaskInMemory(task);
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
        Task task = null;
        String[] line = value.split("\\n");
        for (int i = 0; i < line.length; i++) {
            String[] array = line[i].split(",");
            int id = Integer.parseInt(array[0]);
            TaskType taskType = TaskType.valueOf(array[1]);
            String name = array[2];
            Status status = Status.valueOf(array[3]);
            String description = array[4];
            switch (taskType) {
                case TASK:
                    task = new Task(id, name, description, status);
                    String strTask = array[5];
                    DateTimeFormatter formatterTask = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(strTask, formatterTask);
                    task.setStartTime(dateTime);
                    task.setDuration(Integer.parseInt(array[6]));
                    break;
                case SUBTASK:
                    task = new Subtask(id, name, description, status, Integer.parseInt(array[5]));
                    String strSubtask = array[6];
                    DateTimeFormatter formatterSubtask = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime dateTimeSubtask = LocalDateTime.parse(strSubtask, formatterSubtask);
                    task.setDuration(Integer.parseInt(array[7]));
                    task.setStartTime(dateTimeSubtask);
                    break;
                case EPIC:
                    task = new Epic(id, name, description, status);
                    String strEpic = array[array.length - 2];
                    DateTimeFormatter formatterEpic = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime dateTimeEpic = LocalDateTime.parse(strEpic, formatterEpic);
                    task.setDuration(Integer.parseInt(array[array.length-1]));
                    task.setStartTime(dateTimeEpic);
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
            id.add(task.getId());
        }
        // Преобразовали список id в строку и убрали []
        String history = id.toString();
        history = history.substring(1, history.length() - 1);
        return history;
    }

    /* Метод восстановления id просмотренных тасок менеджера истории из строки */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] line = value.split(", ");
        for (int i = 0; i < line.length; i++) {
            history.add(Integer.valueOf(line[i]));
        }
        return history;
    }

}








