import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import server.HttpTaskServer;
import server.KVServer;
import service.HttpTaskManager;
import service.KVTaskClient;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        /*InMemoryTaskManager*/
/*        LocalDateTime time1 = LocalDateTime.of(2023, 03, 1, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 2, 15, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 3, 19, 00);
        LocalDateTime time4 = LocalDateTime.of(2023, 03, 4, 19, 00);
        LocalDateTime time5 = LocalDateTime.of(2023, 03, 4, 21, 00);

        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task(inMemoryTaskManager.setId(), "Task1", "", Status.NEW, time1, 2);
        Task task2 = new Task(inMemoryTaskManager.setId(), "Task2", "", Status.NEW, time2, 2);
        Epic epic1 = new Epic(inMemoryTaskManager.setId(), "Epic1", "", Status.NEW, time3, 2);
        Subtask subtask1 = new Subtask(inMemoryTaskManager.setId(), "Sub1", "", Status.DONE, time4, 2, 2);
        Subtask subtask2 = new Subtask(inMemoryTaskManager.setId(), "Sub2", "", Status.NEW, time5, 2,2);


        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);

 */
        KVServer kvServer = new KVServer();

        kvServer.start();
        String url = "http://localhost:8078";
        HttpTaskManager httpManager = new HttpTaskManager(url);
        HttpTaskServer httpTaskServer = new HttpTaskServer();
    }
}
