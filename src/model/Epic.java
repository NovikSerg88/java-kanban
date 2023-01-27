package model;

import service.TaskManager;

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
}
