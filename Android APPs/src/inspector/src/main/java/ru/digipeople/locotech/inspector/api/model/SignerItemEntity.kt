package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель исполнителя с флагом в работе
 * TODO испльзуется как заглушка в демо-версии приложения
 *
 * @author Kashonkov Nikita
 */
class SignerItemEntity {
    /**
     * Исполнитель
     */
    @SerializedName("worker")
    lateinit var signer: SignerEntity
    /**
     * Флаг того что есть в подписантах
     */
    @SerializedName("isChecked")
    var isInWork = false
}