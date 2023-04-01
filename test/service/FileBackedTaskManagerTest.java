package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    static final String PATH = "resources/DataTest.csv";

    @Override
    FileBackedTaskManager createManager() {
        Path path = Path.of(PATH);
        File file = new File(String.valueOf(path));
        return new FileBackedTaskManager(file);
    }

    @BeforeEach
    public void beforeEach() {
        taskManager = createManager();
    }

    @Test
    void save() {
        // Создание менеджера
        taskManager.addTask(task1);

        // Сохранение менеджера в файл
        taskManager.save();

        // Проверка существования файла
        Path filePath = Paths.get(PATH);
        assertTrue(Files.exists(filePath));

        // Проверка содержимого файла
        try {
            BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
            String expectedContent = "id,type,name,status,description,epic, startTime, duration\n" +
                    "0,TASK,Task1,NEW,Test,null,0\n\n";
            assertEquals(expectedContent, fileContent.toString());

            // Удаление файла
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл.");
        }
    }

    @Test
    void loadFromFile() {
        // Создание временного файла
        Path path = Path.of(PATH);
        File tempFile = new File(String.valueOf(path));

        // Создание тестовых данных
        LocalDateTime time1 = LocalDateTime.of(2023, 03, 30, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 30, 17, 00);

        subtask1.setDuration(2);
        subtask1.setStartTime(time1);

        task1.setDuration(2);
        task1.setStartTime(time2);

        FileBackedTaskManager fBmanager = new FileBackedTaskManager(tempFile);
        fBmanager.addEpic(epic1);
        fBmanager.addTask(task1);
        fBmanager.addSubtask(subtask1);

        // Заполнение истории
        fBmanager.getTask(0);
        fBmanager.getSubtask(2);
        fBmanager.getEpic(1);

        // Сохранение тестовых данных во временный файл
        fBmanager.save();

        // Загрузка данных из временного файла в новый экземпляр менеджера
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверка, что все объекты из тестовых данных были корректно загружены в новый экземпляр менеджера
        assertNotNull(newManager);
        assertEquals(1, newManager.getListOfEpics().size());
        assertEquals(1, newManager.getListOfTasks().size());
        assertEquals(1, newManager.getListOfSubtasks().size());

        Epic newEpic = newManager.getEpic(epic1.getId());
        assertNotNull(newEpic);
        assertEquals(epic1.getName(), newEpic.getName());
        assertEquals(epic1.getDescription(), newEpic.getDescription());

    }
}
