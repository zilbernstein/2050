package ru.digipeople.locotech.master.ui.activity.remark.adapter.work;

import java.util.List;

import ru.digipeople.locotech.master.model.Work;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера работ
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * конструктор
     */
    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * проверка повторяющихся элементов при перерисовке данных
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Work) {
            Work oldWork = (Work) oldItem;
            Work newWork = (Work) newItem;
            return (oldWork == newWork);
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
