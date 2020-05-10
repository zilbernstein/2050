package ru.digipeople.message.ui.activity.chooseaddressee.adapter;

import java.util.List;

import ru.digipeople.message.model.Contact;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * DiffUtil для контактов
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {

    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * Проверка повторяющихся элементов для ускорения отрисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Contact) {
            Contact oldData = (Contact) oldItem;
            Contact newData = (Contact) newItem;
            return (oldData.getId().equalsIgnoreCase(newData.getId()));
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
