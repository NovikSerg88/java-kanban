package Model;

import Service.TaskManager;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                ", description='" + getDescription() + '\'' +
                ", subtasks='" + subtasks + '\'' +
                '}';
    }

    @Override
    public Status getStatus() {
        for (Integer subtaskId : subtasks.keySet()) {
            if ((Status.NEW.equals(subtasks.get(subtaskId).getStatus())) || (subtasks == null)) {
                return Status.NEW;
            }
            if (Status.DONE.equals(subtasks.get(subtaskId).getStatus())) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
            }
        }
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        for (Integer id : TaskManager.subtasks.keySet()) {
            if (TaskManager.subtasks != null) {
                if (TaskManager.subtasks.get(id).getEpicId().equals(getId())) {
                    subtasks.put(TaskManager.subtasks.get(id).getId(), TaskManager.subtasks.get(id));
                }
            } else return null;
        }
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
