package subTusk;

import java.util.List;
import java.util.Map;

import epic.Epic;
import manager.Manager;
import enums.Status;


public class SubTaskManager extends Manager {

    /**
     * Создаем подзадачу
     *
     * @return
     */
    public SubTask createSubTask(String description, String name, Enum<Status> statusEnum, long epicId) {
        long newIndex = increaseIntId();
        SubTask subTask = new SubTask(newIndex, description, name, statusEnum, epicId);
        subTasksMap.put(newIndex, subTask);
        return subTask;
    }

    /**
     * Переводим статус подзадачи в "в процессе" и обновляем статус эпика
     *
     * @param subTask экземпляр подзадачи
     */
    public void startSubTask(SubTask subTask) {
        subTask.setStatusEnum(Status.IN_PROGRESS);
        Epic epic = Manager.epicManager.returnEpicById(subTask.getEpicId());
        Manager.epicManager.updateEpicStatus(epic);
    }

    /**
     * Переводим статус подзадачи в "сделано" и обновляем статус эпика
     *
     * @param subTask экземпляр подзадачи
     */
    public void endSubTask(SubTask subTask) {
        subTask.setStatusEnum(Status.DONE);
        Epic epic = Manager.epicManager.returnEpicById(subTask.getEpicId());
        Manager.epicManager.updateEpicStatus(epic);
    }

    /**
     * Обновляем подзадачу
     *
     * @param subTask
     */
    public void updateSubTask(SubTask subTask) {
        subTasksMap.put(subTask.getId(), subTask);
    }

    /**
     * Возвращаем все подзадачи
     *
     * @return карту всех подзадач
     */
    public Object returnAllSubTasks() {
        return !subTasksMap.isEmpty() ? subTasksMap : null;
    }

    /**
     * Удаляем все подзадачи
     */
    public void removeAllSubTasks() {
        subTasksMap.clear();

        for (Map.Entry<Long, Epic> epic : Manager.epicManager.epicsMap.entrySet()) {
            epic.getValue().setStatusEnum(Status.NEW);
        }
    }

    /**
     * Возвращаем подзадачу по уникальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     * @return определенная подзадача
     */
    public SubTask returnSubTaskById(long subTaskId) {
        return !subTasksMap.isEmpty() ? subTasksMap.get(subTaskId) : null;
    }

    /**
     * Удаляем подзадачу по универсальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     */
    public void removeSubTaskById(long subTaskId) {
        if (!subTasksMap.isEmpty()) {
            subTasksMap.remove(subTaskId);
        }
    }

    /**
     * Возвращаем список подзадач для конкретного эпика
     *
     * @param epicId уникальный идентификатор эпика
     * @return список подзадач
     */
    public List<SubTask> returnSubTasksForEpicById(long epicId) {

        if (!Manager.epicManager.epicsMap.isEmpty()) {
            if (Manager.epicManager.epicsMap.containsKey(epicId)) {
                Epic epic = Manager.epicManager.epicsMap.get(epicId);
                return epic.getSubTaskList();
            }
        }
        return null;
    }
}
