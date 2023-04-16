package service;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void updateEpicsStatusEmptyTest() {
        // Пустой список subtasks
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic emptyEpic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time1, 0);
        taskManager.addEpic(emptyEpic);
        taskManager.updateEpicsStatus(emptyEpic.getId());
        assertEquals(Status.NEW, emptyEpic.getStatus(), "Эпик с пустыми задачами должен иметь статус NEW");
    }

    @Test
    void updateEpicsStatusNEWTest() {
        // Все подзадачи со статусом NEW
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 3, 3, 17, 0);
        Epic allNewEpic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time1, 0);
        Subtask subtaskNew1 = new Subtask(taskManager.setId(), "Subtask1", "Test", Status.NEW, time2, 3, 0);
        Subtask subtaskNew2 = new Subtask(taskManager.setId(), "Subtask2", "Test", Status.NEW, time3, 3, 0);

        taskManager.addEpic(allNewEpic);
        taskManager.addSubtask(subtaskNew1);
        taskManager.addSubtask(subtaskNew2);
        taskManager.updateEpicsStatus(allNewEpic.getId());
        assertEquals(Status.NEW, allNewEpic.getStatus(), "Эпик с задачами NEW должен иметь статус NEW");
    }

    @Test
    void updateEpicsStatusDONETest() {
        // Все подзадачи со статусом DONE
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 3, 3, 17, 0);
        Epic allDoneEpic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time1, 0);
        Subtask subtaskDone1 = new Subtask(taskManager.setId(), "Subtask1", "Test", Status.DONE, time2, 3, 0);
        Subtask subtaskDone2 = new Subtask(taskManager.setId(), "Subtask2", "Test", Status.DONE, time3, 3, 0);

        taskManager.addEpic(allDoneEpic);
        taskManager.addSubtask(subtaskDone1);
        taskManager.addSubtask(subtaskDone2);
        taskManager.updateEpicsStatus(allDoneEpic.getId());

        assertEquals(Status.DONE, allDoneEpic.getStatus(), "Эпик с задачами DONE должен иметь статус DONE");
    }

    @Test
    void updateEpicsStatusDONEAndNEWTest() {
        // Подзадачи со статусом NEW and DONE
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 3, 3, 17, 0);
        Epic inProgressEpic1 = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time1, 0);
        Subtask subtaskNew3 = new Subtask(taskManager.setId(), "Subtask1", "Test", Status.NEW, time2, 3, 0);
        Subtask subtaskDone3 = new Subtask(taskManager.setId(), "Subtask2", "Test", Status.DONE, time3, 3, 0);

        taskManager.addEpic(inProgressEpic1);
        taskManager.addSubtask(subtaskNew3);
        taskManager.addSubtask(subtaskDone3);
        taskManager.updateEpicsStatus(inProgressEpic1.getId());

        assertEquals(Status.IN_PROGRESS, inProgressEpic1.getStatus(), "Эпик с задачами DONE и NEW должен иметь статус DONE IN_PROGRESS");
    }

    @Test
    void updateEpicsStatusIN_PROGRESSTest() {
        // Подзадачи со статусом IN_PROGRESS
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 3, 3, 17, 0);
        Epic inProgressEpic2 = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time1, 0);
        Subtask subtaskInProg1 = new Subtask(taskManager.setId(), "Subtask1", "Test", Status.IN_PROGRESS, time2, 3, 0);
        Subtask subtaskInProg2 = new Subtask(taskManager.setId(), "Subtask2", "Test", Status.IN_PROGRESS, time3, 3, 0);
        taskManager.addEpic(inProgressEpic2);
        taskManager.addSubtask(subtaskInProg1);
        taskManager.addSubtask(subtaskInProg2);
        taskManager.updateEpicsStatus(inProgressEpic2.getId());

        assertEquals(Status.IN_PROGRESS, inProgressEpic2.getStatus(), "Эпик с задачами IN_PROGRESS должен иметь статус IN_PROGRESS");
    }
}

