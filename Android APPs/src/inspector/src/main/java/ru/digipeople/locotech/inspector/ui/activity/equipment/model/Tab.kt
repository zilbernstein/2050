package ru.digipeople.locotech.inspector.ui.activity.equipment.model

import ru.digipeople.locotech.inspector.model.SectionState
/**
 * Статусы секций
 */
enum class Tab {
    /**
     * Все
     */
    ALL,
    /**
     * На ожидании
     */
    WAITING,
    /**
     * В работе
     */
    IN_SERVICE;

    companion object {
        fun ofSectionState(state: SectionState) = when(state) {
            SectionState.WAITING -> WAITING
            SectionState.IN_SERVICE -> IN_SERVICE
            else -> ALL
        }
    }
}