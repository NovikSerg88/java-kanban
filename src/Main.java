import Model.Epic;
import Model.Subtask;
import Model.Task;
import Service.TaskManager;

public class Main {

    public static void main(String[] args) {

        //Создали задачу, епик и две позадачи
        Task task1 = new Task("Задача 1", "Описание задачи 1", Task.Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Task.Status.IN_PROGRESS);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Task.Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Task.Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Task.Status.NEW, 5);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Task.Status.NEW, 5);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Task.Status.IN_PROGRESS, 6);

        //TaskManager
        TaskManager.createTask(task1);
        TaskManager.createTask(task2);
        TaskManager.createSubtask(subtask1);
        TaskManager.createSubtask(subtask2);
        TaskManager.createSubtask(subtask3);
        TaskManager.createEpic(epic1);
        TaskManager.createEpic(epic2);

        System.out.println(TaskManager.tasks);
        System.out.println(TaskManager.subtasks);
        System.out.println(TaskManager.epics);

        TaskManager.deleteTask(0);
        TaskManager.deleteEpic(6);

        System.out.println(TaskManager.tasks);
        System.out.println(TaskManager.subtasks);
        System.out.println(TaskManager.epics);
    }
}
