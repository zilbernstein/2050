package ru.digipeople.locotech.worker.data.section

import android.util.ArrayMap
import ru.digipeople.locotech.worker.data.task.TaskRepository

/**
 * Репозиторий секции
 *
 * @author Kashonkov Nikita
 */
class SectionRepository private constructor() {
    /**
     * Лист оборудования
     */
    val list: MutableList<Section>
    /**
     * Map Оборудования
     */
    val map: ArrayMap<String, Section>

    init {
        val firstSection = Section("Секция 0365А", mutableListOf(TaskRepository.INSTANCE.map.get("Ремонт ТЭД 0123")!!, TaskRepository.INSTANCE.map.get("Ремонт ТЭД 7345")!!))
        val secondSection = Section("Секция 0365В", mutableListOf(TaskRepository.INSTANCE.map.get("Ремонт ТЭД 4563")!!))

        list = mutableListOf(firstSection, secondSection)

        map = ArrayMap()
        map.put(firstSection.id, firstSection)
        map.put(secondSection.id, secondSection)
    }

    companion object {
        val INSTANCE = SectionRepository()
    }
}