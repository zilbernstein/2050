package ru.digipeople.locotech.master.ui.activity.checkwork.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.Work;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера проверки выбранных работ
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
     * проверка изменения состава элементов в списке
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Work) {
            Work oldWork = (Work) oldItem;
            Work newWork = (Work) newItem;
            return (oldWork.getId().equalsIgnoreCase(newWork.getId()));
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }
    /**
     * функция сравнения 2 элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
