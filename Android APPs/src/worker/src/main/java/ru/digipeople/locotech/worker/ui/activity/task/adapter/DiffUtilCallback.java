package ru.digipeople.locotech.worker.ui.activity.task.adapter;

import java.util.List;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;
import ru.digipeople.locotech.worker.model.TMCInWork;

/**
 * Класс определяющий разницу обектов для структуры задания
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * Конструктор
     */
    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * Проверка повторяющихся элементов для ускорения перерисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof TMCInWork) {
            TMCInWork oldTMC = (TMCInWork) oldItem;
            TMCInWork newTMC = (TMCInWork) newItem;
            return (oldTMC.getId().equalsIgnoreCase(newTMC.getId()));
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }
    /**
     * Сравнение двух элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
