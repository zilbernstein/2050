package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 *
 * Замечание
 */
data class CyclicRemark(
        /**
         * Заголовок
         */
        val title: String,
        /**
         * Список работ
         */
        val works: List<WorkModel>
)