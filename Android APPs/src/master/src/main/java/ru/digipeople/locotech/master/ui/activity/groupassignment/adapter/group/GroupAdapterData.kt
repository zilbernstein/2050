package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group

/**
 * Вспомогательный класс адаптера списка работ для группового назначения
 *
 * @author Sostavkin Grisha
 */
class GroupAdapterData : ArrayList<Any>() {
    /**
     * проверка что элемент - группа
     */
    fun isGroupView(position: Int): Boolean {
        val item = get(position)
        return item is WorkView
    }
    /**
     * получения элемента группы
     */
    fun getGroupView(position: Int): WorkView {
        return get(position) as WorkView
    }
    /**
     * проверка что элемент - заголовок
     */
    fun isTitleView(position: Int): Boolean {
        val item = get(position)
        return item is GroupView
    }
    /**
     * получение элемента заголовка
     */
    fun getTitleView(position: Int): GroupView {
        return get(position) as GroupView
    }
}