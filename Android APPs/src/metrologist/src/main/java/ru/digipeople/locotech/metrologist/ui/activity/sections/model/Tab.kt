package ru.digipeople.locotech.metrologist.ui.activity.sections.model

import ru.digipeople.locotech.metrologist.data.model.SectionState
/**
 * Перечисление статусов секции
 */
enum class Tab {
    /**
     * Все
     */
    ALL,
    /**
     * В ожидании
     */
    WAITING,
    /**
     * На обслуживании
     */
    IN_SERVICE;

    companion object {
        fun ofSectionState(state: SectionState) = when (state) {
            SectionState.WAITING -> WAITING
            SectionState.IN_SERVICE -> IN_SERVICE
            else -> ALL
        }
    }
}