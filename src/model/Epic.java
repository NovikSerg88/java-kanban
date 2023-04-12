package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> epicSubtasks = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime, int duration) {
        super(id, name, description, status, startTime, duration);
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.EPIC + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getStartTime() + "," + getDuration() + "," + getSubtasks() + "," + getEndTime();
    }

    public Map<Integer, Subtask> getSubtasks() {
        return epicSubtasks;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setEpicSubtasks(Map<Integer, Subtask> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }
}
