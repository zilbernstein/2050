package ru.digipeople.locotech.technologist.model

import ru.digipeople.locotech.core.data.model.Comment

/**
 * Модель деталей работы
 *
 * @author Sostavkin Grisha
 */
class WorkDetail {
    /**
     * Комментарий
     */
    var comment: Comment? = null
    /**
     * Выбрана работа ли для согласования
     */
    var isWorkApprove = false
    /**
     * Список параметров
     */
    lateinit var parametersList: List<Parameter>
}