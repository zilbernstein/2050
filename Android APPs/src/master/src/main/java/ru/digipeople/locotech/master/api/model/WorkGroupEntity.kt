package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Группа работ
 *
 * @author Sostavkin Grisha
 */
class WorkGroupEntity {
    /**
     * id группы работ
     */
    @SerializedName("work_group_id")
    lateinit var id: String
    /**
     * Название группы работ
     */
    @SerializedName("work_group_name")
    lateinit var workGroupName: String
    /**
     * Список работ в группе
     */
    @SerializedName("work_list")
    lateinit var workListEntity: List<WorkForWorkerAssignmentEntity>
}