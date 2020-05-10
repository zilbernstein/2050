package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group;

import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера списка работ для группового назначения
 *
 * @author Sostavkin Grisha
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * конструктор
     */
    public DiffUtilCallback(GroupAdapterData oldItems, GroupAdapterData newItems) {
        super(oldItems, newItems);
    }
    /**
     * Определение одинаковых элементов, чтобы испоьзовать их повторно
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof GroupView) {
            GroupView oldData = (GroupView) oldItem;
            GroupView newData = (GroupView) newItem;
            return (oldData.getGroup().getId() == (newData.getGroup().getId()) && oldData.getGroup().getWorkGroupName() == (newData.getGroup().getWorkGroupName()));
        } else if (oldItem instanceof WorkView) {
            WorkView oldData = (WorkView) oldItem;
            WorkView newData = (WorkView) newItem;
            return (oldData.getWorkForWorker().getId() == (newData.getWorkForWorker().getId()) && oldData.getWorkGroup().getId() == newData.getWorkGroup().getId());
        } else {
            throw new IllegalArgumentException("Unknown item type = " + oldItem.getClass());
        }
    }
    /**
     * сравнение двух элементов списка
     */
    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return oldItem.equals(newItem);
    }
}