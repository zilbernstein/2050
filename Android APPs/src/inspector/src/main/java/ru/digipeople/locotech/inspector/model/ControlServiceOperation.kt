package ru.digipeople.locotech.inspector.model

import ru.digipeople.locotech.core.data.model.Comment

/**
 * Модель Контрольно-Сервисной операции
 *
 * @author Kashonkov Nikita
 */
class ControlServiceOperation {
    /**
     * Id
     */
    var id = ""

    /**
     * Название
     */
    var title = ""

    /**
     * Статус
     */
    lateinit var status: WorkStatus

    /**
     * Комментарий
     */
    lateinit var comment: Comment

    /**
     * Количество фотографий
     */
    var photoAmount = 0

    /**
     * Показатель нормы
     */
    var rateValue = ""
}