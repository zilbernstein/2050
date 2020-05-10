package ru.digipeople.locotech.worker.ui.activity.choosereason.adapter;

import java.util.List;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;
import ru.digipeople.locotech.worker.model.PauseReason;

/**
 * Класс определяющий разницу обектов для структуры выбора причины
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
     * Проверка повторяющихся элементов для скорости перерисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof PauseReason) {
            PauseReason oldPauseReason = (PauseReason) oldItem;
            PauseReason newPauseReason = (PauseReason) newItem;
            return (oldPauseReason.id.equalsIgnoreCase(newPauseReason.id));
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
