package model;

import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.EPIC + "," + getName() + "," + getStatus() + "," + getDescription() + "," + subtasks;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }
}
