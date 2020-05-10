package ru.digipeople.locotech.inspector.model

/**
 * Модель Оборудования Контрольно-Сервисной операции
 *
 * @author Kashonkov Nikita
 */
class EquipmentCso {
    /**
     * Название
     */
    var title = ""

    /**
     * Список КСО
     */
    lateinit var csoList: List<ControlServiceOperation>
}