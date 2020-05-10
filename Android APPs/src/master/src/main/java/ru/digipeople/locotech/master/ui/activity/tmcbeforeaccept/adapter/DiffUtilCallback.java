package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter;



import ru.digipeople.utils.adapter.BaseDiffUtilCallback;

/**
 * Вспомогательный класс адаптера списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
public class DiffUtilCallback extends BaseDiffUtilCallback<Object> {
    /**
     * Конструктор
     */
    public DiffUtilCallback(AdapterData oldItems, AdapterData newItems) {
        super(oldItems, newItems);
    }
    /**
     * Проверка повторяющихся элементов для перерисовки экрана
     */
    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if (oldItem.getClass() != newItem.getClass()) return false;
        if (oldItem instanceof String) {
            String oldData = (String) oldItem;
            String newData = (String) newItem;
            return oldData.equalsIgnoreCase(newData);
        } else if(oldItem instanceof TMCData){
            TMCData oldData = (TMCData) oldItem;
            TMCData newData = (TMCData) newItem;
            return oldData.getTmc().getId().equalsIgnoreCase(newData.getTmc().getId());
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
