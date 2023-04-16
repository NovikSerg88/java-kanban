//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import exception.ManagerSaveException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private static final String PATH = "resources/DataTest.csv";

    FileBackedTaskManagerTest() {
    }

    @BeforeEach
    public void setUp() {
        taskManager = new FileBackedTaskManager(new File(PATH));
    }

    @Test
    void save() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);
        taskManager.save();
        Path filePath = Paths.get("resources/DataTest.csv");

        try {
            BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
            StringBuilder fileContent = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            reader.close();
            String expectedContent = "id,type,name,status,description,epic, startTime, duration\n0,TASK,task,NEW,test,2023-03-01T17:00,1\n\n";
            Assertions.assertEquals(expectedContent, fileContent.toString());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
        }

    }

    @Test
    void loadFromFileEmptyTaskListTest() {
        try {
            File file = new File("resources/DataTest.csv");
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,epic, startTime, duration\n");
            writer.close();
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            Assertions.assertEquals(0, manager.getListOfTasks().size());
            Assertions.assertEquals(0, manager.getListOfSubtasks().size());
            Assertions.assertEquals(0, manager.getListOfEpics().size());
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }

    @Test
    void loadFromFileEpicWithoutSubtasksTest() {
        try {
            File file = new File("resources/DataTest.csv");
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,startTime,duration,epic\n");
            writer.write("0,EPIC,EpicTest,IN_PROGRESS,Test,2023-03-01T13:00,5,{}\n");
            writer.close();
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            Assertions.assertEquals(0, manager.getListOfTasks().size());
            Assertions.assertEquals(0, manager.getListOfSubtasks().size());
            Assertions.assertEquals(1, manager.getListOfEpics().size());
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }

    @Test
    void loadFromFileEmptyHistoryTest() {
        try {
            File file = new File("resources/DataTest.csv");
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,epic, startTime, duration\n");
            writer.write("0,TASK,Task1,NEW,,2023-03-04T19:00,0\n");
            writer.write("\n");
            writer.write("0");
            writer.close();
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            Assertions.assertEquals(((Task)manager.tasks.get(0)).getId(), ((Task)manager.getHistory().get(0)).getId());
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }
}
