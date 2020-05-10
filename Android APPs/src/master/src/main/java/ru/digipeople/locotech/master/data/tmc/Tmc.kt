package ru.digipeople.locotech.master.data.tmc

/**
 * Модель ТМЦ
 *
 * @author Kashonkov Nikita
 */
data class Tmc(
        /**
         * UUID ТМЦ
         */
        val id: String,
        /**
         * Заголовок ТМЦ
         */
        val title: String,
        /**
         * Статус ТМЦ
         */
        val status: TmcStatus,
        /**
         * Получатель ТМЦ
         */
        val reciver: String,
        /**
         * Запрошенное количество ТМЦ
         */
        val askedAmount: Int,
        /**
         * Норма
         */
        val normal: Int,
        val sectionRemain: Int,
        val stockemain: Int
) {
    override fun equals(other: Any?): Boolean {
        if(other !is Tmc) return false
        return this.id == other.id
    }
}