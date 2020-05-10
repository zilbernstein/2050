package ru.digipeople.locotech.master.model

/**
 * Модель результат замера
 *
 * @author Sostavkin Grisha
 */
class Indicator(
        /**
         * Идентиффикатор показателя
         */
        val measurementId: String,

        /**
         * Идентификатор характеристики
         */
        val characteristicId: String,
        /**
         * Этап
         */
        val measureStage: Int,
        /**
         * Значение показателя
         */
        val measureValue: Any,
        /**
         * Текст комментария
         */
        val comment: String)