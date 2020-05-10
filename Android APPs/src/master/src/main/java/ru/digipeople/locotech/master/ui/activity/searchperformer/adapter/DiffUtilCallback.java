package ru.digipeople.locotech.master.ui.activity.searchperformer.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.PerformerItem;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера
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
     * Проверка повторяющихся элементов для перерисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof PerformerItem) {
            PerformerItem oldPerformer = (PerformerItem) oldItem;
            PerformerItem newPerformer = (PerformerItem) newItem;
            return (oldPerformer.performer.getId().equalsIgnoreCase(newPerformer.performer.getId()))
                    && (oldPerformer.isInWork() == newPerformer.isInWork() && (oldPerformer.getLoadPercent() == newPerformer.getLoadPercent()));
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
