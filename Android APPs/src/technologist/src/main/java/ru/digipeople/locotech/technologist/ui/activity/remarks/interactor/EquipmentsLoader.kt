package ru.digipeople.locotech.technologist.ui.activity.remarks.interactor

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.mapper.EquipmentMapper
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.AdapterData
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.EquipmentModel
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.RemarkModel
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик оборудования
 */
class EquipmentsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = EquipmentMapper.INSTANCE
    /**
     * Загрузка оборудования
     */
    fun loadEquipments(): Single<Result> {
        return thingsWorxWorker.getRemarkAndWork()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { equipments ->
                    val adapterData = AdapterData()
                    equipments.forEach { equipment ->
                        /**
                         * Обработка оборудования
                         */
                        val equipmentModel = EquipmentModel()
                        equipmentModel.equipment = equipment

                        val remarks = mutableListOf<RemarkModel>()
                        equipment.section.forEach { section ->
                            /**
                             * Обработка секций
                             */
                            section.remarkList.forEach {
                                remarks.add(RemarkModel().apply {
                                    remark = it
                                    this.section = section
                                })
                            }
                        }

                        equipmentModel.remarks = remarks
                        adapterData.add(equipmentModel)

                        if (remarks.isNotEmpty()) {
                            equipmentModel.isExpanded = true
                            adapterData.addAll(remarks)
                        }
                    }
                    Result(UserError.NO_ERROR, adapterData)
                }
                .onErrorReturn { error ->
                    /**
                     * Обработа ошибки
                     */
                    val userError = errorBuilder.fromThowable(error)
                    Result(userError, AdapterData())
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val equipments: AdapterData) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}