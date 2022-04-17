package manager;

import entities.Node;
import entities.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    public final int DEEP_OF_HISTORY = 10;
    private final List<Task> arrayListHistory = new ArrayList<>();
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<Task>();
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
            //удалить первый элемент
        }

    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void removeHistory(long id) {
        historyList.removeNode(id);
    }


    private class CustomLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

/*        public void linkFirst(T element) {
            final Node<T> oldHead = head;
            final Node<T> newNode = new Node<T>(element, null, oldHead);
            head = newNode;
            if (oldHead == null)
                tail = newNode;
            else
                oldHead.prev = newNode;
            size++;
        }*/

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

        public int size() {
            return this.size;
        }

        void removeNode(long nodeId) {
            nodeMap.remove(nodeId);
        }

        //собирает все задачи из связанного кастомного в обычный список
        public List<Task> getTasks() {
/*            for (Node node : nodeMap.values()) {
                arrayListHistory.add((Task) node.data);
            }*/
            Node node = historyList.head;
            arrayListHistory.add((Task) node.data);
            while (node.next != null) {
                arrayListHistory.add((Task) node.next.data);
                if (node.next == null) {
                    break;
                } else {
                    node = node.next;
                }
            }

            return arrayListHistory;
        }
    }
}
