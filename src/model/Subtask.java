package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, Status status, LocalDateTime startTime, int duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.SUBTASK + "," + getName() + ","+ getStatus() + "," + getDescription()  + "," + getStartTime() + "," + getDuration() + "," + getEpicId();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

}
