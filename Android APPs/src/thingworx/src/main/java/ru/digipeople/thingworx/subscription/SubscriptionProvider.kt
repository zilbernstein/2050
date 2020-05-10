package ru.digipeople.thingworx.subscription

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Интерфейс для осуществления подписки/отписки на обновлении данных
 *
 * @author Kashonkov Nikita
 */
@Singleton
class SubscriptionProvider @Inject constructor(val subscriptionThing: SubscriptionThing) {
    /**
     * Предоставляет подписку на изменение данных о количестве работ в разделе "Срочно"
     */
    fun urgentAmountSubscription(): Observable<Int> = subscriptionThing.subscribeUrgentAmountRenewServer()

    /**
     * Предоставляет подписку на изменение данных об оборудовании
     */
    fun equipmentSubscription(): Observable<Unit> = subscriptionThing.subscribeEquipmentRenewServer()

    /**
     * Предоставляет подписку на изменение данных о замечаниях
     */
    fun remarkSubscription(): Observable<Unit> = subscriptionThing.subscribeRemarkRenewServer()

    /**
     * Предоставляет подписку на изменение данных о работах
     */
    fun workSubscription(): Observable<Unit> = subscriptionThing.subscribeWorkRenewServer()

    /**
     * Предоставляет подписку на изменение данных в части согласования работ
     */
    fun approvalSubscription(): Observable<Unit> = subscriptionThing.subscribeApproavalRenewService()
}