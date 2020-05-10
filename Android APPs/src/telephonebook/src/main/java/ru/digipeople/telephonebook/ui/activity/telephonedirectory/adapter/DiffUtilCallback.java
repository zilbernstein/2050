package ru.digipeople.telephonebook.ui.activity.telephonedirectory.adapter;

import java.util.List;

import ru.digipeople.telephonebook.model.Contact;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Класс для сравнения объектов
 *
 * @author Sostavkin Grisha
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * конструктор
     */
    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /**
     * проверка повторяющихся элементов для ускорения перерисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Contact) {
            Contact oldContact = (Contact) oldItem;
            Contact newContact = (Contact) newItem;
            return (oldContact.id.equals(newContact.id));
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