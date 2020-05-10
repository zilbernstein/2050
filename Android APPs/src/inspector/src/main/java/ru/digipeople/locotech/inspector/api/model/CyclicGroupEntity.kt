package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель Группы цикловых работ
 * @author Kashonkov Nikita
 */
class CyclicGroupEntity {
    /**
     * Id группы
     */
    @SerializedName("id_group_works")
    lateinit var id: String
    /**
     * Название группы
     */
    @SerializedName("group_work_name")
    lateinit var groupName: String
    /**
     * Список работ в группе
     */
    @SerializedName("work_list")
    lateinit var works: List<WorkEntity>
}