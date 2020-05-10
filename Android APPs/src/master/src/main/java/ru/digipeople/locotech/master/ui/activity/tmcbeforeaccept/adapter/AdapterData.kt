package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter

/**
 * Вспомогательный класс адаптера списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
class AdapterData() : ArrayList<Any>() {
    /**
     * Проверка что элемент - ТМЦ
     */
    fun isTmcData(position: Int): Boolean {
        val item = get(position)
        return item is TMCData
    }
    /**
     * Получение элемента ТМЦ
     */
    fun getTmc(position: Int): TMCData {
        return get(position) as TMCData
    }
    /**
     * Проверка что элемент - заголовок
     */
    fun isTitle(position: Int): Boolean{
        val item = get(position)
        return item is String
    }
    /**
     * Получение элемента заголовка
     */
    fun getTitle(position: Int): String{
        return get(position) as String
    }
}