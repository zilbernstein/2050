package ru.digipeople.locotech.inspector.data

/**
 * Класс с данными для заглушки КТ
 * @author Kashonkov Nikita
 */
class ControlPointRepository private constructor(){
    val controlPointList: List<ControlPoint>

    init {
        /**
         * Список контрольных точек
         */
        controlPointList = listOf(
                ControlPoint("Отклонения по кругу катания не более, мм","7"),
                ControlPoint("Разница между правой и левой стороны колеса не более, мм","2"),
                ControlPoint("Ширина шва, максимум, мм","4,0"),
                ControlPoint("Ширина шва, минимум, мм","3,5"),
                ControlPoint("Обработано образивом","да"))
    }
    /**
     * Создание сущности
     */
    private object Holder {
        val INSTANCE = ControlPointRepository()
    }

    companion object {
        val INSTANCE: ControlPointRepository by lazy { Holder.INSTANCE }
    }
}