package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryHistoryManagerTest extends HistoryManagerTest<HistoryManager>{

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
    }
}