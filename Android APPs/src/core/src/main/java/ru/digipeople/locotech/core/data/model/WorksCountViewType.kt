package ru.digipeople.locotech.core.data.model

import androidx.annotation.ColorRes
import ru.digipeople.locotech.core.R
/*
        * Раскраска количества работ по вкладкам ддля МП Мастер и МП Выпуск на линию
        */
enum class WorksCounterViewType(val value: String, @ColorRes val color: Int) {
    /*
        * На согласовании
        */
    IN_MATCHING("works_in_matching_count", R.color.appOrange),
    /*
        * Нет исполнителя
        */
    NO_PERFORMER("works_no_performer_count", R.color.appOrange),
    /*
        * Назначено
        */
    ASSIGNED("works_assigned_count", R.color.appBlack),
    /*
        * В работе
        */
    IN_PROGRESS("works_in_progress_count", R.color.appBlack),
    /*
        * Завершено
        */
    COMPLETED("works_completed_count", R.color.appOrange),
    /*
        * Принято мастером
        */
    ACCEPTED_MASTER("works_accepted_master_count", R.color.appGreen),
    /*
        * Все
        */
    ALL("works_all_count", R.color.appBlack),
    /*
        * Цикловые работы
        */
    CYCLIC("works_cyclic_count", R.color.appBlack),
    /*
            * Инспекионный контроль
            */
    REMARKS_INSPECTION("works_remarks_inspection_count", R.color.appBlack),
    /*
            * Принято ОТК
            */
    REMARKS_SLD("works_remarks_sld_count", R.color.appBlack),
    /*
            * Принятол РЖД
            */
    REMARKS_RZD("works_remarks_rzd_count", R.color.appBlack);

    companion object {
        fun of(value: String) = values().find { it.value == value }
                ?: throw IllegalArgumentException("Unable to find works count view type by value = $value")
    }
}