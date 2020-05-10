package ru.digipeople.locotech.worker.data.task

import android.util.ArrayMap
import ru.digipeople.locotech.worker.data.tmc.TMCRepository
import java.util.*

/**
 * Репозиторий задания
 *
 * @author Kashonkov Nikita
 */
class TaskRepository private constructor() {
    /**
     * Список работ
     */
    val list: MutableList<Task>
    /**
     * Map работ
     */
    val map: ArrayMap<String, Task>

    init {
        val firstTask = Task("Ремонт ТЭД 0123", "3С5К 13523450 0365А", "Секция 0365А", TaskStatus.PAUSED, Date().time - 300000, 0.5, 600000, TMCRepository.INSTANCE.list)
        val secondTask = Task("Ремонт ТЭД 7345", "3С5К 13523450 0365А", "Секция 0365А", TaskStatus.PAUSED, Date().time - 300000, 0.3, 600000, TMCRepository.INSTANCE.list)
        val thirdTask = Task("Ремонт ТЭД 4563", "3С5К 13523450 0365В", "Секция 0365В", TaskStatus.NEW, 0L, 0.0, 600000, TMCRepository.INSTANCE.list)

        list = mutableListOf(firstTask, secondTask, thirdTask)

        map = ArrayMap()
        map.put(firstTask.id, firstTask)
        map.put(secondTask.id, secondTask)
        map.put(thirdTask.id, thirdTask)
    }

    companion object {
        val INSTANCE = TaskRepository()
    }
}