package ru.digipeople.locotech.master.ui.activity.tmclist.adapter;

import java.util.List;

import ru.digipeople.locotech.master.model.TMCInWork;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера списка ТМЦ
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
     * Проверка повторяющихся элементов для перерисовки списка
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof TMCInWork) {
            TMCInWork oldTmc = (TMCInWork) oldItem;
            TMCInWork newTmc = (TMCInWork) newItem;
            return (oldTmc.getId().equalsIgnoreCase(newTmc.getId()));
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }
    /**
     * Сравнение элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
