package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель отчета
 *
 * @author Michael Solenov
 */
class ReportEntity {
    @SerializedName("report_id")
    lateinit var id: String

    @SerializedName("report_name")
    lateinit var name: String

    @SerializedName("report_url")
    lateinit var url: String
}