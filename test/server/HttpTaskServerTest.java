package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;
import service.HttpTaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class HttpTaskServerTest {
    private static HttpClient client;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

    @BeforeAll
    static void beforeAll() throws IOException {
        new KVServer().start();
        new HttpTaskServer();
        client = HttpClient.newHttpClient();
    }


    @Test
    public void taskHandlerTests() throws IOException, InterruptedException {
        /*Проверили метод POST:addTask*/
        HttpTaskManager manager = new HttpTaskManager("http://localhost:" + KVServer.PORT);
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(manager.setId(), "task", "test", Status.NEW, time, 1);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("POST request: добавлена задача " + task.getName(), response.body());

        /*Проверили метод POST:update Task*/
        Task newTask = new Task(task.getId(), "taskUpdated", "test", Status.IN_PROGRESS, time, 1);
        json = gson.toJson(newTask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("POST request: обновлена задача " + newTask.getName(), response.body());

        /*Проверили метод GET*/
        url = URI.create("http://localhost:8080/tasks/task/?id=0");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task getTask = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode());
        assertEquals(0, getTask.getId());

        /*Проверили метод DELETE*/
        url = URI.create("http://localhost:8080/tasks/task/?id=0");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("DELETE request: удалена задача id" + task.getId(), response.body());
    }

    @Test
    public void epicHandlerTests() throws IOException, InterruptedException {
        /*Проверили метод POST:addEpic*/
        HttpTaskManager manager = new HttpTaskManager("http://localhost:" + KVServer.PORT);
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(1, "epic", "test", Status.NEW, time, 1);
        URI url = URI.create("http://localhost:8080/epics/epic/");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("POST request: добавлен эпик " + epic.getName(), response.body());

        /*Проверили метод POST:update Epic*/
        Epic newEpic = new Epic(1, "epicUpdated", "test", Status.NEW, time, 1);
        json = gson.toJson(newEpic);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("POST request: обновлен эпик " + newEpic.getName(), response.body());

        /*Проверили метод GET*/
        url = URI.create("http://localhost:8080/epics/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic getEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode());
        assertEquals(1, getEpic.getId());

        /*Проверили метод DELETE*/
        url = URI.create("http://localhost:8080/epics/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("DELETE request: удален эпик id" + epic.getId(), response.body());
    }

    @Test
    public void subtaskHandlerTests() throws IOException, InterruptedException {
        /*Проверили метод POST:addSubtask*/
        HttpTaskManager manager = new HttpTaskManager("http://localhost:" + KVServer.PORT);
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(manager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        epicPostTemplate(epic);
        Subtask subtask = new Subtask(manager.setId(), "subtask", "test", Status.NEW, time, 1, epic.getId());
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        String json = gson.toJson(subtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("POST request: добавлена задача " + subtask.getName(), response.body());

        /*Проверили метод POST:update Subtask*/
        Subtask newSubtask = new Subtask(subtask.getId(), "subtaskUpdated", "test", Status.NEW, time, 12, 0);
        json = gson.toJson(newSubtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("POST request: обновлена задача " + newSubtask.getName(), response.body());

        /*Проверили метод GET*/
        url = URI.create("http://localhost:8080/subtasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask getSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, getSubtask.getId());

        /*Проверили метод DELETE*/
        url = URI.create("http://localhost:8080/subtasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("DELETE request: удалена подзадача id" + subtask.getId(), response.body());
    }

    @Test
    public void tasksHandlerTests() throws IOException, InterruptedException {
        HttpTaskManager manager = new HttpTaskManager("http://localhost:" + KVServer.PORT);
        LocalDateTime taskTime = LocalDateTime.of(2023, 3, 1, 1, 0);
        LocalDateTime subtaskTime = LocalDateTime.of(2023, 3, 1, 3, 0);
        Epic epic = new Epic(manager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Task task = new Task(manager.setId(), "task", "test", Status.NEW, taskTime, 1);
        Subtask subtask = new Subtask(manager.setId(), "subtaskUpdated", "test", Status.NEW, subtaskTime, 2, epic.getId());
        epicPostTemplate(epic);
        subtasksPostTemplate(subtask);
        taskPostTemplate(task);

        /*Проверили метод GET*/
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertEquals(2, tasks.size());
        assertEquals("task", tasks.get(0).getName());

        /*Проверили метод DELETE*/
        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("DELETE request: все задачи удалены", response.body());
    }

    @Test
    public void tasksHistoryHandlerTest() throws IOException, InterruptedException {
        LocalDateTime taskTime = LocalDateTime.of(2023, 3, 1, 1, 0);
        LocalDateTime subtaskTime = LocalDateTime.of(2023, 3, 1, 3, 0);
        Epic epic = new Epic(0, "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Task task = new Task(1, "task", "test", Status.NEW, taskTime, 1);
        Subtask subtask = new Subtask(2, "subtaskUpdated", "test", Status.NEW, subtaskTime, 2, epic.getId());
        epicPostTemplate(epic);
        subtasksPostTemplate(subtask);
        taskPostTemplate(task);

        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task getTask = gson.fromJson(response.body(), Task.class);

        url = URI.create("http://localhost:8080/subtasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask getSubtask = gson.fromJson(response.body(), Subtask.class);

        /*Проверили метод GET*/
        url = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(response.body(), taskType);
        System.out.println(history);

        assertEquals(200, response.statusCode());
        assertEquals(getTask.getId(), history.get(0).getId());
        assertEquals(getSubtask.getId(), history.get(1).getId());
    }

    private void epicPostTemplate(Epic epic) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/epic/");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void taskPostTemplate(Task task) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void subtasksPostTemplate(Subtask subtask) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        String json = gson.toJson(subtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

