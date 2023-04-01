package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.SUBTASK + "," + getName() + ","+ getStatus() + "," + getDescription() + "," + epicId + "," + getStartTime() + "," + getDuration();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

}
