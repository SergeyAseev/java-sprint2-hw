package manager;

import java.util.List;

import entities.Epic;
import entities.SubTask;
import enums.Status;

public class EpicManager extends Manager {

    /**
     * Создаем эпик-задачу
     *
     * @return
     */
    public Epic createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList) {
        long newIndex = increaseIntId();
        Epic epic = new Epic(newIndex, description, name, statusEnum, subTaskList);
        epicsMap.put(newIndex, epic);
        return epic;
    }

    /**
     * Обновляем эпик
     *
     * @param epic эпик, который надо обновить
     */
    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    /**
     * обновляем статусы эпик-задач
     */
    public void updateEpicStatus(Epic epic) {

        if (epicsMap.isEmpty()) {
            epic.setStatusEnum(Status.NEW);
            return;
        }

        boolean doneStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTask.getStatusEnum().equals(Status.DONE));

        boolean newStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTask.getStatusEnum().equals(Status.NEW));

        if (doneStatus) {
            epic.setStatusEnum(Status.DONE);
        } else if (newStatus) {
            epic.setStatusEnum(Status.NEW);
        } else {
            epic.setStatusEnum(Status.IN_PROGRESS);
        }
    }

    /**
     * Возвращаем все эпики
     *
     * @return карту всех эпиков
     */
    public Object returnAllEpics() {
        return !epicsMap.isEmpty() ? epicsMap : null;
    }

    /**
     * Удаляем все эпики и их подзадачи
     */
    public void removeAllEpics() {
        subTaskManager.removeAllSubTasks();
        epicsMap.clear();
    }

    /**
     * Возвращаем эпик по уникальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     * @return определенный эпик
     */
    public Epic returnEpicById(long epicId) {
        return !epicsMap.isEmpty() ? epicsMap.get(epicId) : null;
    }

    /**
     * Удаляем эпик по универсальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     */
    public void removeEpicById(long epicId) {
        if (!epicsMap.isEmpty()) {
            epicsMap.remove(epicId);
        }
    }
}
