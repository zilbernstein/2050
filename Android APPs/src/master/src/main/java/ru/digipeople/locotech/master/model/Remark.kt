package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Comment
import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель замечания
 *
 * @author Kashonkov Nikita
 */
class Remark {
    /**
     * Id замечания
     */
    lateinit var id: String
    /**
     * Наименование замечания
     */
    lateinit var title: String
    /**
     * Статус замечания
     */
    lateinit var status: RemarkStatus
    /**
     * Комментарий к замечанию
     */
    var comment: Comment? = null
    /**
     * Процент выполнения по локомтиву или ЛО
     */
    var equipmentPercent = 0
    /**
     * Осталось времени на работу
     */
    var equipmentTimeLeft = 0L
    /**
     * Все исполнители по замечанию
     */
    var performerList = listOf<Performer>()
    /**
     * Количетво фотографий
     */
    var photoCount = 0
    /**
     * Возможность добавление работ
     */
    var isAddWorkEnable = false
    /**
     * Сисок работ в замечании
     */
    lateinit var workList: List<Work>
    /**
     * Автор замечания
     */
    lateinit var author: String
}