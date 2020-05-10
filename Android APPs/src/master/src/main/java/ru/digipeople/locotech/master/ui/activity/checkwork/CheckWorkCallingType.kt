package ru.digipeople.locotech.master.ui.activity.checkwork

/**
 * Класс с вариантами вызова экрана проверки работ
 *
 * @author Kashonkov Nikita
 */
enum class CheckWorkCallingType(val value: Int) {
    /**
     * Проверка работ перед согласованием
     */
    CALLING_WORK_APPROVE(1),
    /**
     * Проверка работ перед запуском
     */
    CALLING_WORK_START(2),
    /**
     * Проверка работ перед принятием
     */
    CALLING_WORK_ACCEPT(3);

    companion object {
        fun valueOf(code: Int) = values().firstOrNull { it.value == code }
    }

}