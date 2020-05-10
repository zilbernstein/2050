package ru.digipeople.locotech.worker.data.tmc

/**
 * Модель товарно-материальных ценностей
 *
 * @author Kashonkov Nikita
 */
data class TMC(
        /**
         * Id ресурса
         */
        val id: String,
        /**
         * Количество
         */
        var quantity: String
)