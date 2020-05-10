package ru.digipeople.locotech.master.ui.activity.remark

/**
 * Типы сортировки
 *
 * @author Kashonkov Nikita
 */
enum class SortType(val code: Int) {
    /**
     * Исполнитель
     */
    PERFORMER (1),

    /**
     * Срочность
     */
    URGENCY (2),

    /**
     * Название работ
     */
    TITLE (3);


    companion object {
        fun valueOf(code: Int) = values().firstOrNull { it.code == code }
    }
}