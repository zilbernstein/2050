package ru.digipeople.locotech.worker.ui.activity.mytask.adapter;

import android.view.View;

import java.util.List;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;
import ru.digipeople.locotech.worker.model.Equipment;
import ru.digipeople.locotech.worker.model.Section;
import ru.digipeople.locotech.worker.model.Work;

/**
 * Класс определяющий разницу обектов для оборудования
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
     * Проверка повторяющихся элементов для ускорения отрисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof Section) {
            Section oldSection = (Section) oldItem;
            Section newSection = (Section) newItem;
            return (oldSection.sectionId.equalsIgnoreCase(newSection.sectionId));
        }else if(oldItem instanceof Work){
            Work oldWork = (Work) oldItem;
            Work newWork = (Work) newItem;
            return (oldWork.workId.equalsIgnoreCase(newWork.workId) && oldWork.workStatus == newWork.workStatus);
        } else if (oldItem instanceof Equipment) {
            Equipment oldEquipment = (Equipment) oldItem;
            Equipment newEquipment = (Equipment) newItem;
            return (oldEquipment.equipmentId.equalsIgnoreCase(newEquipment.equipmentId));
        } else if (oldItem instanceof TitleItem) {
            TitleItem oldTitle = (TitleItem) oldItem;
            TitleItem newTitle = (TitleItem) newItem;
            return (oldTitle.getTitle().equalsIgnoreCase(newTitle.getTitle()));
        } else if (oldItem instanceof View) {
            return true;
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
