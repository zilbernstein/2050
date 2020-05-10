package ru.digipeople.locotech.worker.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Класс [ChecklistResponse] - модель ответа метода work_checklist МП Сотрудник
 *
 * @author Sostavkin Grisha
 */
class ChecklistResponse {

    /**
     * Список чеклиста
     */
    @SerializedName("checklist")
    var items = emptyList<ChecklistItemEntity>()

    /**
     * Ссылка на пдф файл
     */
    @SerializedName("techprocess_url")
    var techProcessUrl = ""

    class ChecklistItemEntity {
        /**
         * Идентификатор строки чеклиста
         */
        @SerializedName("checklist_item_id")
        var id = ""
        /**
         * Наименование строки чеклиста
         */
        @SerializedName("checklist_item_name")
        var name = ""
        /**
         * Ссылка строки чеклиста
         */
        @SerializedName("checklins_item_link")
        var link = ""
        /**
         * Признак проверки строки чеклиста
         */
        @SerializedName("is_checked")
        var isChecked = false
    }
}