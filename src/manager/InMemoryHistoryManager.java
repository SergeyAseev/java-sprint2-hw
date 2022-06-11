package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import entities.Task;

public class InMemoryHistoryManager implements HistoryManager {
    HashMap<Long, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * Добавление просмотренных задач в список "История задач":
     * удаление задачи из списка,если она там есть,
     * а затем добавление её в конец двусвязного списка.
     */
    @Override
    public void addHistory(Task task) {
        final Node<Task> node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    /**
     * Удаление задачи из просмотра.
     */
    @Override
    public void removeHistory(Task task) {
        final Node<Task> node = history.remove(task.getId());
        if (node != null) {
            removeNode(node);
        }
    }

    /**
     * Удаление Node — узла связного списка.
     */
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
    }

    /**
     * Добавление задач в конец списка.
     */
    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        history.put(newNode.data.getId(), newNode);
    }

    /**
     * Реализация метода должна перекладывать задачи
     * из связного списка в ArrayList для формирования ответа.
     */
    public List<Task> getTasks() {
        final List<Task> newHistory = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            newHistory.add(node.data);
            node = node.next;
        }
        return newHistory;
    }
}