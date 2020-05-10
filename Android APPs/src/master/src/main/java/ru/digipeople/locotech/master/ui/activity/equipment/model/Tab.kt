package ru.digipeople.locotech.master.ui.activity.equipment.model

import ru.digipeople.locotech.master.model.SectionState
/**
 * Виды состояния секции (на обслуживании, в ожидании, все)
 */
enum class Tab {
    /**
     * все
     */
    ALL,
    /**
     * в ожидании
     */
    WAITING,
    /**
     * на обслуживании
     */
    IN_SERVICE;

    companion object {
        /**
         * определение состояния
         */
        fun ofSectionState(state: SectionState) = when (state) {
            SectionState.WAITING -> WAITING
            SectionState.IN_SERVICE -> IN_SERVICE
            else -> ALL
        }
    }
}