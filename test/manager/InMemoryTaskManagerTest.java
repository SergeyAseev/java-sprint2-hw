package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void initInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    void inMemoryTaskManagerTest() {
        taskManager = new InMemoryTaskManager();

        assertEquals(0, taskManager.returnAllTasks().size());
        assertEquals(0, taskManager.returnAllEpics().size());
        assertEquals(0, taskManager.returnAllSubTasks().size());
        assertEquals(0,taskManager.getHistory().size());
    }
}