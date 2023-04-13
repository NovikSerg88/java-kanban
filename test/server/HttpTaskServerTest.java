package server;

import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;


class HttpTaskServerTest {
    private final String URL = "http://localhost:8080/tasks/task";

    @BeforeAll
    static void beforeAll() throws IOException {
       new HttpTaskServer().start();
       new KVServer().start();
    }


}