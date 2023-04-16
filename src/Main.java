
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//Проект был случайно удален и восстановлен из папки out. Прошу рассмотреть проект без части тестов,
// т.к. он в процессе восстановления
        new KVServer().start();
        new HttpTaskServer();
    }
}
