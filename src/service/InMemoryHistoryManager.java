package service;

import model.Node;
import model.Task;

import java.util.HashMap;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private Node<Task> first;
    private Node<Task> last;

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        historyMap.put(last.data.getId(), last);
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }

    private List<Task> getTasks() {
        List<Task> arrayList = new ArrayList<>();
        for (Node<Task> currentNode = first; currentNode != null; currentNode = currentNode.next) {
            arrayList.add(currentNode.data);
        }
        return arrayList;
    }

    private void unlink(Node<Task> currentNode) {
        final Node<Task> next = currentNode.next;
        final Node<Task> prev = currentNode.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            currentNode.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            currentNode.next = null;
        }
        currentNode.data = null;
    }

    private void removeNode(Node<Task> node) {
        for (Node<Task> currentNode = first; currentNode != null; currentNode = currentNode.next) {
            if (node.data.equals(currentNode.data)) {
                unlink(currentNode);
            }
        }
    }
}

