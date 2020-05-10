package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель сводной информации
 *
 * @author Sostavkin Grisha
 */
class SummaryEntity {
    /**
     * Название
     */
    @SerializedName("inspection_name")
    lateinit var name: String
    /**
     * Принято
     */
    @SerializedName("accepted")
    var accepted: Int = 0
    /**
     * Не принято отк
     */
    @SerializedName("not_accepted_otk")
    var declinedOtk: Int = 0
    /**
     * Не принято ржд
     */
    @SerializedName("not_accepted_rzd")
    var declinedRzd: Int = 0
    /**
     * Тип
     */
    @SerializedName("inspection_type")
    lateinit var type: String
}