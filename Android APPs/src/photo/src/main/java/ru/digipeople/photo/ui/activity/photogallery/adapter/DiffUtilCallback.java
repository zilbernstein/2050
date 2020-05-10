package ru.digipeople.photo.ui.activity.photogallery.adapter;

import android.graphics.Bitmap;
import java.util.List;

import ru.digipeople.photo.model.Photo;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Класс определяющий разницу обектов для фото
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /*
     * Конструктор
     */
    public DiffUtilCallback(List<Object> oldItems, List<Object> newItems) {
        super(oldItems, newItems);
    }
    /*
     * Проверка повторяющихся элементов для ускорения отрисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Photo) {
            Photo oldWork = (Photo) oldItem;
            Photo newWork = (Photo) newItem;
            return (oldWork.getUrl()).equalsIgnoreCase(newWork.getUrl());
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }
    /*
     * Сравнение двух элементов
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
