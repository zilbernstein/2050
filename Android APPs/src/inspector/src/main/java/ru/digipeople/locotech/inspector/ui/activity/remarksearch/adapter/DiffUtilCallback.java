package ru.digipeople.locotech.inspector.ui.activity.remarksearch.adapter;

import java.util.List;

import ru.digipeople.locotech.inspector.model.RemarkFromCatalog;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Класс определяющий разницу обектов для добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {

    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * проверка повторяющихся элементов для ускорения отрисовки
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
     * сравнение двух элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
