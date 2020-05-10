package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель списка работ без исполнителей и списка бригад
 *
 * @author Sostavkin Grisha
 */
class WorksAndBrigadesEntity {
    /**
     * Список групп работ
     */
    @SerializedName("work_group_list")
    lateinit var workGroupList: List<WorkGroupEntity>
    /**
     * Список бригад
     */
    @SerializedName("brigades_list")
    lateinit var brigadeList: List<BrigadeEntity>
    /**
     * Ограничение на максимальное число сотрудников
     */
    @SerializedName("worker_limit")
     var workerLimit: Int = 0
}