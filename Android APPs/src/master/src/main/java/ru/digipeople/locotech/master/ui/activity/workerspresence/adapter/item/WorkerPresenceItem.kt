package ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item

import java.util.*
/**
 * элемент явки сотрудников
 */
data class WorkerPresenceItem(
        /**
         * UUID
         */
        val id: String,
        /**
         * Наименование
         */
        val name: String,
        /**
         * Номер
         */
        val number: String,
        /**
         * Приход
         */
        val timeIn: Date?,
        /**
         * Уход
         */
        val timeOut: Date?,
        /**
         * Начало рабочей смены
         */
        val timeBegin: Date?,
        /**
         * Завершение рабочей смены
         */
        val timeFinish: Date?,
        /**
         * рабочие часы
         */
        val workTime: Date?,
        /**
         * Ночные рабочие часы
         */
        val timeNight: Date?,
        /**
         * явка
         */
        val presence: Boolean,
        /**
         * ночная смена
         */
        val night: Boolean
)