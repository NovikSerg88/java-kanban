package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                ", description='" + getDescription() + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }

    public Integer getEpicId() {
        return epicId;
    }

}
