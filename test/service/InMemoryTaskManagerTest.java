package service;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    LocalDateTime time1 = LocalDateTime.of(2023, 03, 1, 17, 00);
    LocalDateTime time2 = LocalDateTime.of(2023, 03, 2, 17, 00);
    LocalDateTime time3 = LocalDateTime.of(2023, 03, 3, 19, 00);


   @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void updateEpicsStatusTest() {
        // Пустой список subtasks
        Epic emptyEpic = new Epic(1, "Epic1", "Test", Status.NEW, LocalDateTime.MAX, 0);
        taskManager.addEpic(emptyEpic);
        taskManager.updateEpicsStatus(emptyEpic.getId());
        assertEquals(Status.NEW, emptyEpic.getStatus(), "Эпик с пустыми задачами должен иметь статус NEW");

        // Все подзадачи со статусом NEW
        Subtask subtaskNew1 = new Subtask(1, "Subtask1", "Test", Status.NEW, time1, 1, 3);
        Subtask subtaskNew2 = new Subtask(2, "Subtask2", "Test", Status.NEW,time2, 1, 3);
        Epic allNewEpic = new Epic(3, "Epic1", "Test", Status.NEW, LocalDateTime.MAX, 0);
        taskManager.addEpic(allNewEpic);
        taskManager.addSubtask(subtaskNew1);
        taskManager.addSubtask(subtaskNew2);
        taskManager.updateEpicsStatus(allNewEpic.getId());
        assertEquals(Status.NEW, allNewEpic.getStatus(), "Эпик с задачами NEW должен иметь статус NEW");

        // Все подзадачи со статусом DONE
        Subtask subtaskDone1 = new Subtask(4, "Subtask1", "Test", Status.DONE,time1, 1,6);
        Subtask subtaskDone2 = new Subtask(5, "Subtask2", "Test", Status.DONE, time2, 1, 6);
        Epic allDoneEpic = new Epic(6, "Epic1", "Test", Status.NEW, LocalDateTime.MAX, 0);
        taskManager.addEpic(allDoneEpic);
        taskManager.addSubtask(subtaskDone1);
        taskManager.addSubtask(subtaskDone2);
        taskManager.updateEpicsStatus(allDoneEpic.getId());
        assertEquals(Status.DONE, allDoneEpic.getStatus(), "Эпик с задачами DONE должен иметь статус DONE");

        // Подзадачи со статусом NEW and DONE
        Subtask subtaskNew3 = new Subtask(7, "Subtask1", "Test", Status.NEW, time1, 1, 9);
        Subtask subtaskDone3 = new Subtask(8, "Subtask2", "Test", Status.DONE, time2, 1, 9);
        Epic inProgressEpic1 = new Epic(9, "Epic1", "Test", Status.NEW, LocalDateTime.MAX, 0);
        taskManager.addEpic(inProgressEpic1);
        taskManager.addSubtask(subtaskNew3);
        taskManager.addSubtask(subtaskDone3);
        taskManager.updateEpicsStatus(inProgressEpic1.getId());
        assertEquals(Status.IN_PROGRESS, inProgressEpic1.getStatus(), "Эпик с задачами DONE и NEW должен иметь статус DONE IN_PROGRESS");

        // Подзадачи со статусом IN_PROGRESS
        Subtask subtaskInProg1 = new Subtask(10, "Subtask1", "Test", Status.IN_PROGRESS, time1, 1, 12);
        Subtask subtaskInProg2 = new Subtask(11, "Subtask2", "Test", Status.IN_PROGRESS, time2, 1, 12);
        Epic inProgressEpic2 = new Epic(12, "Epic1", "Test", Status.NEW, LocalDateTime.MAX, 0);
        taskManager.addEpic(inProgressEpic2);
        taskManager.addSubtask(subtaskInProg1);
        taskManager.addSubtask(subtaskInProg2);
        taskManager.updateEpicsStatus(inProgressEpic2.getId());
        assertEquals(Status.IN_PROGRESS, inProgressEpic2.getStatus(), "Эпик с задачами IN_PROGRESS должен иметь статус IN_PROGRESS");
    }
}
