package manager;

import entities.Node;
import entities.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private final Map<Long, Node> nodeMap = new HashMap<>();


    @Override
    public void addHistory(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        long currentTaskId = task.getId();
        if (nodeMap.containsKey(currentTaskId)) {
            removeHistory(currentTaskId);
        }

        nodeMap.put(task.getId(), historyList.linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void removeHistory(long id) {

        Node currentNode = nodeMap.get(id);
        //начало
        if (currentNode == historyList.head) {
            if (currentNode ==historyList.tail) {
                historyList.head = null;
                historyList.tail = null;
                return;
            }
            final Node<Task> newFirst = historyList.head;
            newFirst.prev = null;
            historyList.head = newFirst;
            return;
        }
        //конец
        if (currentNode == historyList.tail) {
            historyList.tail = currentNode.prev;
            currentNode.next = null;
            return;
        }
        //середина
        final Node<Task> prev = currentNode.prev;
        final Node<Task> next = currentNode.next;
        prev.next = next;
        next.prev = prev;
        historyList.removeNode(currentNode);

/*        if (Objects.isNull(currentNode)) {
            return;
        }

        if (currentNode.prev == null) {
            historyList.head = historyList.head.next;
        } else if (currentNode.next == null) {
            Node oldPrev = currentNode.prev;
            oldPrev.next = null;
        } else {
            Node oldPrev = currentNode.prev;
            Node oldNext = currentNode.next;
            oldPrev.next = oldNext;
            oldNext.prev = oldPrev;
        }
        historyList.removeNode(currentNode);*/
    }


    private class CustomLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        //Добавляем задачу в конец списка
        private Node linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<T>(element, oldTail, null);
            tail = newNode;

            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;

            return newNode;
        }

        void removeNode(Node node) {
            nodeMap.values().remove(node);
        }

        //Собираем все задачи из связанного кастомного в обычный список
        public List<Task> getTasks() {
            final List<Task> arrayListHistory = new ArrayList<>();

            Node node = head;
            if (Objects.isNull(node)) {
                return arrayListHistory;
            }
            while (node.next != null) {
                arrayListHistory.add((Task) node.data);
                node = node.next;
            }

            return arrayListHistory;
        }
    }
}
