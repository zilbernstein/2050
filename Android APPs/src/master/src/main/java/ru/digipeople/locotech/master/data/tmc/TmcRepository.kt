package ru.digipeople.locotech.master.data.tmc

/**
 * Модель заглушки для теста ТМЦ
 * @author Kashonkov Nikita
 */
class TmcRepository {
    /**
     * Лист работ
     */
    var list: MutableList<Tmc>
    /**
     * Инициализация данных
     */
    init {
        list = mutableListOf()

        list.add(Tmc("1", "ПРИПОЙ ДЛЯ СВАРКИ ШВОВ ПОС-30, грамм", TmcStatus.ORDERED, "", 450, 500, 43, 4785))
        list.add(Tmc("2", "ЭЛЕКТРОДЫ МАРКИ ОМА-2, шт", TmcStatus.RECEIVED, "Слуцкий А.В.", 38, 25, 54, 25))
        list.add(Tmc("3", "ТМЦ 4", TmcStatus.COMPILED, "", 45, 50, 540, 47800))
        list.add(Tmc("4", "ТМЦ 5", TmcStatus.REFUSED, "", 0, 50, 2, 15))
        list.add(Tmc("5", "ТМЦ 6", TmcStatus.ORDERED, "", 190, 200, 71, 1000))
    }

    companion object {
        val INSTANCE = TmcRepository()
    }
}