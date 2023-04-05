package service;

import exception.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    static final String PATH = "resources/DataTest.csv";

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(new File(PATH));
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
    void loadFromFileEmptyTaskListTest() {
        // Создаем пустой файл с заголовком
        try {
            File file = new File(PATH);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,epic, startTime, duration\n");
            writer.close();
            // Загружаем менеджер из файла
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            // Проверяем, что в менеджере нет задач
            assertEquals(0, manager.getListOfTasks().size());
            assertEquals(0, manager.getListOfSubtasks().size());
            assertEquals(0, manager.getListOfEpics().size());
            // Удаляем файл
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }

    @Test
    void loadFromFileEpicWithoutSubtasksTest() {
        // Создаем файл с одним эпиком
        try {
            File file = new File(PATH);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,epic, startTime, duration\n");
            writer.write("0,EPIC,EpicTest,IN_PROGRESS,Test,{},2023-03-01T13:00,5\n");
            writer.close();
            // Загружаем менеджер из файла
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            // Проверяем, что в менеджере нет задач
            assertEquals(0, manager.getListOfTasks().size());
            assertEquals(0, manager.getListOfSubtasks().size());
            assertEquals(1, manager.getListOfEpics().size());
            // Удаляем файл
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }

    @Test
    void loadFromFileEmptyHistoryTest() {
        try {
            // Создаем файл с одним эпиком
            File file = new File(PATH);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("id,type,name,status,description,epic, startTime, duration\n");
            writer.write("0,TASK,Task1,NEW,,2023-03-04T19:00,0\n");
            writer.write("\n");
            writer.write("0");
            writer.close();
            // Загружаем менеджер из файла
            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
            // Проверяем, что задача добавилась в историю
            assertEquals(manager.tasks.get(0).getId(), manager.getHistory().get(0).getId());
            // Удаляем файл
            file.delete();
        } catch (IOException e) {
            throw new ManagerSaveException("Запись не возможна.", e);
        }
    }
}
