package ru.digipeople.locotech.master.ui.activity.equipment.adapter.item

import ru.digipeople.locotech.master.model.Equipment
import ru.digipeople.locotech.master.model.Section

/**
 * Модель секции для адаптера
 *
 * @author Kashonkov Nikita
 */
data class SectionItem(
        /**
         * Оборудование
         */
        var equipment: Equipment,
        /**
         * Секция
         */
        var section: Section
)