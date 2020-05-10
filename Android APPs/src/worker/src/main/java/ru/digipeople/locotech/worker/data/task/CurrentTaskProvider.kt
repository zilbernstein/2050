package ru.digipeople.locotech.worker.data.task

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для хранения текущей смены
 *
 * @author Kashonkov Nikita
 */
@Singleton
class CurrentTaskProvider @Inject constructor() {
    var task: Task? = null
}