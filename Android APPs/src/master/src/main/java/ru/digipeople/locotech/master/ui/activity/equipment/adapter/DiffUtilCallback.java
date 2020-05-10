package ru.digipeople.locotech.master.ui.activity.equipment.adapter;

import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.DividerItem;
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.EquipmentItem;
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.LineEquipmentItem;
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.SectionItem;
import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {

    public DiffUtilCallback(AdapterData oldItems, AdapterData newItems) {
        super(oldItems, newItems);
    }
    /**
     * определение наличия данного итема в списке
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        /**
         * элемент - оборудование (локомотив)
         */
        if (oldItem instanceof EquipmentItem) {
            EquipmentItem oldData = (EquipmentItem) oldItem;
            EquipmentItem newData = (EquipmentItem) newItem;
            return (oldData.getEquipment().getId().equalsIgnoreCase(newData.getEquipment().getId())
                    && oldData.isExpanded() == newData.isExpanded());
            /**
             * элемент - линейное оборудование
             */
        } else if (oldItem instanceof LineEquipmentItem) {
            LineEquipmentItem oldData = (LineEquipmentItem) oldItem;
            LineEquipmentItem newData = (LineEquipmentItem) newItem;
            return (oldData.getEquipment().getId().equalsIgnoreCase(newData.getEquipment().getId()));
            /**
             * элемент - секция
             */
        } else if (oldItem instanceof SectionItem) {
            SectionItem oldData = (SectionItem) oldItem;
            SectionItem newData = (SectionItem) newItem;
            return (oldData.getSection().getId().equalsIgnoreCase(newData.getSection().getId()));
            /**
             * элемент - разделитель
             */
        } else if (oldItem instanceof DividerItem) {
            return true;
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }

    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}
