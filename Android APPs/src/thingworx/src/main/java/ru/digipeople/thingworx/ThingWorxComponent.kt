package ru.digipeople.thingworx

import ru.digipeople.thingworx.subscription.SubscriptionThing
import ru.digipeople.thingworx.subscription.SubscriptionThingWorker

/**
 * Компонент модуля ThingWorx
 *
 * @author Kashonkov Nikita
 */
interface ThingWorxComponent {

    fun baseThingWorx(): BaseThingWorx

    fun subscriptionThingWorker(): SubscriptionThingWorker

    fun subscriptionThing(): SubscriptionThing

    companion object {
        private lateinit var instance: ThingWorxComponent

        fun get(): ThingWorxComponent = instance

        fun set(thingWOrxComponent: ThingWorxComponent) {
            instance = thingWOrxComponent
        }
    }
}