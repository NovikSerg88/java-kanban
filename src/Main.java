import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        /*InMemoryTaskManager*/
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task(inMemoryTaskManager.setId(), "Task1", "", Status.NEW);
        Task task2 = new Task(inMemoryTaskManager.setId(), "Task2", "", Status.NEW);
        Epic epic1 = new Epic(inMemoryTaskManager.setId(), "Epic1", "", Status.NEW);
        Subtask subtask1 = new Subtask(inMemoryTaskManager.setId(), "Sub1", "", Status.DONE, 2);
        Subtask subtask2 = new Subtask(inMemoryTaskManager.setId(), "Sub2", "", Status.NEW, 2);
        Subtask subtask3 = new Subtask(inMemoryTaskManager.setId(), "Sub3", "", Status.NEW, 2);

        LocalDateTime time1 = LocalDateTime.of(2023, 03, 1, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 2, 15, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 3, 19, 00);
        LocalDateTime time4 = LocalDateTime.of(2023, 03, 4, 19, 00);
        LocalDateTime time5 = LocalDateTime.of(2023, 03, 4, 21, 00);

        subtask1.setDuration(2);
        subtask1.setStartTime(time1);

        subtask2.setDuration(2);
        subtask2.setStartTime(time2);

        subtask3.setDuration(1);
        subtask3.setStartTime(time3);

        task1.setDuration(2);
        task1.setStartTime(time4);

        task2.setDuration(2);
        task2.setStartTime(time5);

        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
    }
}
