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
            linkLast(task);
        } else {
            linkLast(task);
        }
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

    public void linkLast(Task task) {
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

    public List<Task> getTasks() {
        List<Task> arrayList = new ArrayList<>();
        for (Node<Task> x = first; x != null; x = x.next) {
            arrayList.add(x.data);
        }
        return arrayList;
    }

    public void unlink(Node<Task> x) {
        final Node<Task> next = x.next;
        final Node<Task> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
    }

    public void removeNode(Node<Task> n) {
        if (n == null) {
            for (Node<Task> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                }
            }
        } else {
            for (Node<Task> x = first; x != null; x = x.next) {
                if (n.data.equals(x.data)) {
                    unlink(x);
                }
            }
        }
    }
}

