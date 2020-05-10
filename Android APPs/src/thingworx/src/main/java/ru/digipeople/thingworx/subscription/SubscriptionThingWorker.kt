package ru.digipeople.thingworx.subscription

/**
 * Интерфейс, содержащий методы подписки/отписки клиента от [SubscriptionThing]
 *
 * @author Kashonkov Nikita
 */
interface SubscriptionThingWorker {
    /**
     * Подписывает клиента на [SubscriptionThing]
     */
    fun bind()

    /**
     * Отписывает клиента от [SubscriptionThing]
     */
    fun unbind()
}