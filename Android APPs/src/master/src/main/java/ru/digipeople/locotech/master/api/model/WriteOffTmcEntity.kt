package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель для списания ТМЦ из работы
 * @author Kashonkov Nikita
 */
class WriteOffTmcEntity {
    /**
     * ID работы
     */
    @SerializedName("id_work")
    lateinit var workId: String
    /**
     * Название работы
     */
    @SerializedName("work_name")
    lateinit var workName: String
    /**
     * список ТМЦ
     */
    @SerializedName("tmc_list")
    var tmcList: List<TMCInWorkEntity> = listOf()
}