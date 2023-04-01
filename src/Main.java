import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws IOException {

        /*InMemoryTaskManager*/

        TaskManager tm = Managers.getDefault();
        Task task1 = new Task(tm.setId(), "Task1", "", Status.NEW);
        Task task2 = new Task(tm.setId(), "Task2", "", Status.NEW);
        Epic epic1 = new Epic(tm.setId(), "Epic1", "", Status.NEW);
        Subtask subtask1 = new Subtask(tm.setId(), "Sub1", "", Status.DONE, 2);
        Subtask subtask2 = new Subtask(tm.setId(), "Sub2", "", Status.NEW, 2);
        Subtask subtask3 = new Subtask(tm.setId(), "Sub3", "", Status.NEW, 2);
        Epic epic2 = new Epic(tm.setId(), "", "", Status.NEW);


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time1 = LocalDateTime.of(2023, 03, 30, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 30, 17, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 30, 19, 00);

        subtask1.setDuration(2);
        subtask1.setStartTime(time1);

        subtask2.setDuration(2);
        subtask2.setStartTime(time2);

        subtask3.setDuration(1);
        subtask3.setStartTime(time3);

        task1.setStartTime(now);

        tm.addEpic(epic1);
        tm.addSubtask(subtask1);
        tm.addSubtask(subtask2);
        tm.addSubtask(subtask3);
        tm.addTask(task1);
        tm.addTask(task2);

        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getDuration());
        System.out.println(epic1.getEndTime());

        System.out.println(tm.getPrioritizedTasks());
    }
}
