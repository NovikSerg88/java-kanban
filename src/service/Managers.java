package service;

import server.KVServer;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

/*    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }

 */

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
