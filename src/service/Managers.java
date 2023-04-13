package service;

import server.KVServer;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
