package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс для адаптера списка бригад и исполнителей
 *
 * @author Sostavkin Grisha
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * конструктор
     */
    public DiffUtilCallback(BrigadeAdapterData oldItems, BrigadeAdapterData newItems) {
        super(oldItems, newItems);
    }
    /**
     * определение одинаковых элементов. чтобы их не перерисовывать
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof BrigView) {
            BrigView oldData = (BrigView) oldItem;
            BrigView newData = (BrigView) newItem;
            return (oldData.getBrigade().getId().equals(newData.getBrigade().getId()));
        } else if (oldItem instanceof WorkerView) {
            WorkerView oldData = (WorkerView) oldItem;
            WorkerView newData = (WorkerView) newItem;
            return (oldData.getWorker().getPerformer().getId()==(newData.getWorker().getPerformer().getId()));
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