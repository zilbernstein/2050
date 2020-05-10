package ru.digipeople.locotech.master.ui.activity.allworklist.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.WorkFromCatalog;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**Вспомогательный класс для адаптера списка работ, определеющий измененные элементы для перерисовки
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
     * Проверка новый ли элемент или тот же самый
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof WorkFromCatalog) {
            WorkFromCatalog oldWork = (WorkFromCatalog) oldItem;
            WorkFromCatalog newWork = (WorkFromCatalog) newItem;
            return (oldWork.getId().equalsIgnoreCase(newWork.getId()));
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }

    @Override
    /**
     * Соответствие отображаемого контента
     */
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
