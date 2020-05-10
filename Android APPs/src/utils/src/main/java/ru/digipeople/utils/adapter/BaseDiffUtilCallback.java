package ru.digipeople.utils.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;


import java.util.List;

/**
 * Базовый класс для реализаций {@link DiffUtil.Callback}.
 *
 * @author Sostavkin Grisha
 */
public abstract class BaseDiffUtilCallback<T> extends DiffUtil.Callback {

    private final List<T> oldItems;
    private final List<T> newItems;

    public BaseDiffUtilCallback(List<T> oldItems, List<T> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }
    /**
     * Получить размер старого списка
     */
    @Override
    public int getOldListSize() {
        return oldItems.size();
    }
    /**
     * Получить размер нового списка
     */
    @Override
    public int getNewListSize() {
        return newItems.size();
    }
    /**
     * Сравненеи двух элементов
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItems.get(oldItemPosition), newItems.get(newItemPosition));
    }
    /**
     * Сравнение старого и нового списков
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldItems.get(oldItemPosition), newItems.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return getChangePayload(oldItems.get(oldItemPosition), newItems.get(newItemPosition));
    }

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Nullable
    protected Object getChangePayload(T oldItem, T newItem) {
        return null;
    }
}
