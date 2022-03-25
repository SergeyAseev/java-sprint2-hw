import java.util.List;

public class EpicManager extends Manager {

    /**
     * Создаем эпик-задачу
     *
     * @return
     */
    protected Epic createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList) {
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
/*  Согласно ТЗ:
    При обновлении можете считать, что на вход подаётся новый объект,
    который должен полностью заменить старый. К примеру, метод для
    обновления эпика может принимать эпик в качестве входных данных public void updateEpic(Epic epic).
    Если вы храните эпики в HashMap, где ключами являются идентификаторы, то обновление —
    это запись нового эпика epics.put(epic.getId(), epic)).*/
    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    /**
     * обновляем статусы эпик-задач
     */
    protected void updateEpicStatus(Epic epic) {

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
    protected Object returnAllEpics() {
        return !epicsMap.isEmpty() ? epicsMap : null;
    }

    /**
     * Удаляем все эпики и их подзадачи
     */
    protected void removeAllEpics() {
        subTaskManager.removeAllSubTasks();
        epicsMap.clear();
    }

    /**
     * Возвращаем эпик по уникальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     * @return определенный эпик
     */
    protected Epic returnEpicById(long epicId) {
        return !epicsMap.isEmpty() ? epicsMap.get(epicId) : null;
    }

    /**
     * Удаляем эпик по универсальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     */
    protected void removeEpicById(long epicId) {
        if (!epicsMap.isEmpty()) {
            epicsMap.remove(epicId);
        }
    }
}
