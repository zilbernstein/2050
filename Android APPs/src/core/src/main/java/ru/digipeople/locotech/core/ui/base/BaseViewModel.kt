package ru.digipeople.locotech.core.ui.base

import androidx.lifecycle.ViewModel
import ru.digipeople.logger.LoggerFactory

/**
 * Базовый класс для ViewModel
 */
abstract class BaseViewModel : ViewModel() {
    /**
 * подключение логирования
 */
    protected val logger = LoggerFactory.getLogger(this::class)

    private var isStarted: Boolean = false
    /**
     * старт
     */
    fun start() {
        if (!isStarted) {
            isStarted = true
            onStart()
        }
    }

    protected abstract fun onStart()

}