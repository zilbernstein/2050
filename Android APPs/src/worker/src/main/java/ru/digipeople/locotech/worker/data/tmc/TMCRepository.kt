package ru.digipeople.locotech.worker.data.tmc

import android.util.ArrayMap

/**
 * Репозиторий ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCRepository private constructor() {
    /**
     * Лист ресурсов
     */
    val list: MutableList<TMC>
    /**
     * Map ресурсов
     */
    val map: ArrayMap<String, TMC>

    init {

        val firstTmc = TMC("Набор инструментов А205", "1шт")
        val secondTmc = TMC("Катушка индуктивности", "1шт")
        val thirdTmc = TMC("Припой", "50г")
        list = mutableListOf(firstTmc, secondTmc, thirdTmc)

        map = ArrayMap()
        map.put(firstTmc.id, firstTmc)
        map.put(secondTmc.id, secondTmc)
        map.put(thirdTmc.id, thirdTmc)

    }

    companion object {
        val INSTANCE = TMCRepository()
    }
}