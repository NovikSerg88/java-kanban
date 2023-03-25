package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of("resources/Tasks.csv");
        File file = new File(String.valueOf(path));
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

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
        fileBackedTaskManager.getTask(1);

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
            fileWriter.write("id,type,name,status,description,epic\n");
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
    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
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
                    break;
                case SUBTASK:
                    task = new Subtask(id, name, description, status, Integer.parseInt(array[5]));
                    break;
                case EPIC:
                    task = new Epic(id, name, description, status);
                    break;
                default:
                    System.out.println("Неизвестный тип задачи");
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








