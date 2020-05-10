package ru.digipeople.locotech.worker.data.reason

import android.util.ArrayMap

/**
 * Репозиторий причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class ReasonRepository private constructor() {
    /**
     * Лист причин
     */
    val list: MutableList<Reason>
    /**
     * Map причин
     */
    val map: ArrayMap<Int, Reason>

    init {
        val firstReason = Reason(1, "Обеденный перерыв")
        val secondReason = Reason(2, "Отдых и личные надобности")
        val thirdReason = Reason(3, "Нехватка ТМЦ")
        val fourthReason = Reason(4, "Нехватка инструмента")
        val fithReason = Reason(5, "Другая причина или работа")

        list = mutableListOf(firstReason, secondReason, thirdReason, fourthReason, fithReason)

        map = ArrayMap()
        map.put(firstReason.id, firstReason)
        map.put(secondReason.id, secondReason)
        map.put(thirdReason.id, thirdReason)
        map.put(fourthReason.id, fourthReason)
        map.put(fithReason.id, fithReason)
    }

    companion object {
        val INSTANCE = ReasonRepository()
    }
}