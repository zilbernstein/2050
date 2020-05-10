package ru.digipeople.thingworx.subscription

import ru.digipeople.thingworx.BaseThingWorx

/**
 * Базовая реализация [SubscriptionThingWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultSubscriptionThingWorker(val baseThingWorx: BaseThingWorx, val subscriptionThing: SubscriptionThing) : SubscriptionThingWorker {
    override fun bind() {
//        val client = baseThingWorx.client!!
//        subscriptionThing.init(client)
//        client.bindThing(subscriptionThing)
    }

    override fun unbind() {
//        val client = baseThingWorx.client
//        client?.unbindThing(subscriptionThing)
//        subscriptionThing.removeClient()
    }
}