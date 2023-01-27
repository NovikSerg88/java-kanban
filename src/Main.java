import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        //Создали задачу, епик и две позадачи
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.DONE, 2);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, 2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.DONE, 3);
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", Status.NEW, 3);
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5", Status.DONE, 3);
        //TaskManager
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask5);
        taskManager.updateEpicsStatus();


        System.out.println(taskManager.tasks);
        System.out.println(taskManager.subtasks);
        System.out.println(taskManager.epics);

//        taskManager.deleteTask(0);
        taskManager.deleteSubtask(7);


//        System.out.println(taskManager.tasks);
//        System.out.println(taskManager.subtasks);
//        System.out.println(taskManager.epics);


    }
}
