package ru.digipeople.locotech.master.ui.activity.performance.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.Work;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера исполнения
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {

    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * поиск повторяющихся элементов при перерисовке данных
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Work) {
            Work oldWork = (Work) oldItem;
            Work newWork = (Work) newItem;
            return (oldWork.getId().equalsIgnoreCase(newWork.getId())) && (oldWork.status == newWork.status);
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }

    /**
     * сравнение двух элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
