package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Comment
import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель работы
 *
 * @author Kashonkov Nikita
 */
class Work {
    /**
     * Идентификатор работы
     */
    lateinit var id: String
    /**
     * Наименование работы
     */
    lateinit var title: String
    /**
     * Статус работы
     */
    lateinit var status: WorkStatus
    /**
     * Исполнители
     */
    lateinit var performers: List<Performer>
    /**
     * Комментарий
     */
    var comment: Comment? = null
    /**
     * Нормативное время
     */
    var normalTime = 0L
    /**
     * Оставшееся время
     */
    var remainTime = 0L
    /**
     * Id причины остановки
     */
    lateinit var stopReasonId: String
    /**
     * Флаг необходимости фотографий
     */
    var isPhotoRequired = false
    /**
     * Количество фотографий
     */
    var photoCount = 0
    /**
     * Флаг того, что работа просрочена
     */
    var isExpired = false
    /**
     * Название оборудование
     */
    lateinit var equipmentName: String
    /**
     * Номер наряда
     */
    var outfitNumber: String? = ""
    /**
     * Процент от первоначальной работы
     */
    var workPartPercent: Int = 100
    /**
     * Количество повторов
     */
    var repeats = 0
    /**
     * Замеры
     */
    var measurements: String? = ""

    var hasTmc = false

    var hasMeasurements = false

    var hasMpi = false



    override fun equals(other: Any?): Boolean {
        if (other !is Work) return false
        if (this.id.equals(other.id, true) && this.status == other.status
                && checkPerformers(other)) return true
        return false
    }



    private fun checkPerformers(work: Work): Boolean {
        if (this.performers.size != work.performers.size) return false
        this.performers.forEach { performer ->
            var workersAreTheSame = false
            work.performers.forEach { it ->
                if (performer.id == it.id) {
                    workersAreTheSame = true
                    return@forEach
                }
            }
            if (!workersAreTheSame) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }


}