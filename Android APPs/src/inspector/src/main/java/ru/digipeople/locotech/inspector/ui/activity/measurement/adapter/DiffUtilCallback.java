package ru.digipeople.locotech.inspector.ui.activity.measurement.adapter;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера Замеров
 * Определяет разницу обектов
 *
 * @author Sostavkin Grisha
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {

    public DiffUtilCallback(AdapterData oldItems, AdapterData newItems) {
        super(oldItems, newItems);
    }
    /**
     * проверка повторяющихся элементов для ускорения отрисовки
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof TitleModel) {
            TitleModel oldData = (TitleModel) oldItem;
            TitleModel newData = (TitleModel) newItem;
            return (oldData.getTitle().equals(newData.getTitle()));
        } else if (oldItem instanceof MeasurementModel) {
            MeasurementModel oldData = (MeasurementModel) oldItem;
            MeasurementModel newData = (MeasurementModel) newItem;
            return (oldData.getMeasurement().measurementId.equalsIgnoreCase(newData.getMeasurement().measurementId));
        } else if (oldItem instanceof DividerModel) {
            return true;
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
