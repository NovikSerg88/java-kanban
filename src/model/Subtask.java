package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + Type.SUBTASK + "," + getName() + ","+ getStatus() + "," + getDescription() + "," + epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public Type getType() {
        return Type.SUBTASK;
    }

}
