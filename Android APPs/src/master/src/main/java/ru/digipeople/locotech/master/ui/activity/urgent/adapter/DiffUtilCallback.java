package ru.digipeople.locotech.master.ui.activity.urgent.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.Work;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера срочно
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
        if (oldItem instanceof Work) {
            Work oldWork = (Work) oldItem;
            Work newWork = (Work) newItem;
            Boolean isIdTheSame = oldWork.getId().equalsIgnoreCase(newWork.getId());
            Boolean isStatusTheSame = oldWork.status == newWork.status;
            Boolean isCommentTheSame = oldWork.getComment().equals(newWork.getComment());
            return ( isIdTheSame && isStatusTheSame && isCommentTheSame);
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
