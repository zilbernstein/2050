package ru.digipeople.locotech.master.ui.activity.remarksearch.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.RemarkFromCatalog;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера добавления / создания замечания
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
     * Проверка повторяющихся элементов, для перерисовки экрана
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof RemarkFromCatalog) {
            RemarkFromCatalog oldWork = (RemarkFromCatalog) oldItem;
            RemarkFromCatalog newWork = (RemarkFromCatalog) newItem;
            return (oldWork.getId() == newWork.getId());
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
