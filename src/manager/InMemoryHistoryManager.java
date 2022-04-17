package manager;

import entities.Node;
import entities.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    public final int DEEP_OF_HISTORY = 10;
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private final Map<Long, Node> nodeMap = new HashMap<>();


    @Override
    public void addHistory(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        long currentTaskId = task.getId(); //ПОМНИ, ЧТО ЭТО НЕ ТОЛЬКО TASK!!!
        if (nodeMap.containsKey(currentTaskId)) {
            removeHistory(currentTaskId);
        }

        nodeMap.put(task.getId(), historyList.linkLast(task));

        if (historyList.size > DEEP_OF_HISTORY) {
            removeHistory(historyList.head.data.getId());
        }

    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void removeHistory(long id) {

        Node currentNode = nodeMap.get(id);

        if (currentNode == historyList.head) {
            historyList.head = historyList.head.next;
        } else if (currentNode == historyList.tail) {
            historyList.tail = historyList.tail.prev;
        } else {
            Node oldPrev = currentNode.prev;
            Node oldNext = currentNode.next;
            oldPrev.next = oldNext;
            oldNext.prev = oldPrev;
            historyList.removeNode(currentNode);
        }
        historyList.removeNode(currentNode);
    }


    private class CustomLinkedList<T>{

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        //Добавляем задачу в конец списка
        private Node linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<T>(element, oldTail,null);
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

        //собирает все задачи из связанного кастомного в обычный список
        public List<Task> getTasks() {
            final List<Task> arrayListHistory = new ArrayList<>();

            Node node = head;
            if (Objects.isNull(node)) {
                return arrayListHistory;
            }
            while (true) {
                arrayListHistory.add((Task) node.data);
                if (node.next == null) {
                    break;
                }
                node = node.next;
            }

            return arrayListHistory;
        }
    }
}
