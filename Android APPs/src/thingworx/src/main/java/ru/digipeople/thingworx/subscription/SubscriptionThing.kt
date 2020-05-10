package ru.digipeople.thingworx.subscription

import com.thingworx.communications.client.AndroidConnectedThingClient
import com.thingworx.communications.client.things.VirtualThing
import com.thingworx.metadata.annotations.ThingworxServiceDefinition
import com.thingworx.metadata.annotations.ThingworxServiceParameter
import com.thingworx.metadata.annotations.ThingworxServiceResult
import com.thingworx.types.constants.CommonPropertyNames
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс содержащий тригеры о появлении новых данных на сервере
 * Каждый тригер - отдельный метод VirtualThing, вызываемый с сервера при получении новых данных
 * Слушатели уведомляются об обновлении данных на сервере по средствам RX
 *
 * @author Kashonkov Nikita
 */
@Singleton
class SubscriptionThing @Inject constructor() : VirtualThing() {

    private val urgentAmountSubscriptionSubject = BehaviorSubject.create<Int>()
    private val equipmentSubscriptionSubject = PublishSubject.create<Unit>()
    private val remarkSubscriptionSubject = PublishSubject.create<Unit>()
    private val workSubscriptionSubject = PublishSubject.create<Unit>()
    private val approvalSubscriptionSubject = PublishSubject.create<Unit>()
    /**
     * инициализация
     */
    init {
        initializeFromAnnotations()
    }

    fun init(client: AndroidConnectedThingClient) {
        super.setName("SubscribeThing")
        super.setClient(client)
        urgentAmountSubscriptionSubject.onNext(0)
    }
    /**
     * удаление клиента
     */
    fun removeClient(){
        super.setClient(null)
    }

    /**
     * Тригер события обновления оборудования
     */
    @ThingworxServiceDefinition(name = "equipmentRenewService", description = "Calling when some info about equipment has come")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, baseType = "NOTHING")
    fun equipmentRenewService() {
        equipmentSubscriptionSubject.onNext(Unit)
    }

    /**
     * Тригер обновления замечаний
     */
    @ThingworxServiceDefinition(name = "remarkRenewService", description = "Calling when some info about remark has come")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, baseType = "NOTHING")
    fun remarkRenewService() {
        remarkSubscriptionSubject.onNext(Unit)
    }

    /**
     * Тригер обновления работы
     */
    @ThingworxServiceDefinition(name = "workRenewService", description = "Calling when some info about work has come")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, baseType = "NOTHING")
    fun workRenewService() {
        workSubscriptionSubject.onNext(Unit)
    }

    /**
     * Тригер обновления количества работ в разделе "Срочно"
     */
    @ThingworxServiceDefinition(name = "urgentAmountRenewService", description = "Calling when some info about new amount of urgent has come")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, baseType = "NOTHING")
    fun urgentAmountRenewService(
            @ThingworxServiceParameter(name = "urgentAmount", description = "Amount of urgent work",
                    baseType = "TEXT") urgentAmount: String) {
        urgentAmountSubscriptionSubject.onNext(urgentAmount.toInt())
    }

    /**
     * Тригер обновления количества работ в части согласования
     */
    @ThingworxServiceDefinition(name = "approvalRenewService", description = "Calling when some info about new amount of urgent has come")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, baseType = "NOTHING")
    fun approvalRenewService() {
        approvalSubscriptionSubject.onNext(Unit)
    }


    fun subscribeUrgentAmountRenewServer(): Observable<Int> = urgentAmountSubscriptionSubject
    fun subscribeEquipmentRenewServer(): Observable<Unit> = equipmentSubscriptionSubject
    fun subscribeRemarkRenewServer(): Observable<Unit> = remarkSubscriptionSubject
    fun subscribeWorkRenewServer(): Observable<Unit> = workSubscriptionSubject
    fun subscribeApproavalRenewService(): Observable<Unit> = approvalSubscriptionSubject
}