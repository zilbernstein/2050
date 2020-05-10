package ru.digipeople.locotech.inspector.ui.activity.inspection

/**
 * Класс перечисление - закладки в инспекционном контроле
 *
 * @author Kashonkov Nikita
 */
enum class StartTab(val code: Int) {
    //Циклические работы
    CYCLIC_WORK(0),
    //Замчения ОТК
    REMARK_OTK(1),
    //Замечания РЖД
    REMARK_RZD(2);

    companion object {
        fun valueOf(code: Int) = values().firstOrNull { it.code == code }
    }
}